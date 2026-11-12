package com.example.service;

import com.example.entity.Company;
import com.example.entity.Package;
import com.example.entity.Payment;
import com.example.entity.User;
import com.example.enums.PaymentStatus;
import com.example.repository.CompanyRepository;
import com.example.repository.PackageRepository;
import com.example.repository.PaymentRepository;
import com.example.repository.UserRepository;
import com.example.vnpay.IpnUrl.IpnResponse;
import com.example.vnpay.PaymentRequest;
import com.example.vnpay.VnPayConfig;
import com.example.vnpay.VnPayService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository payments;
    private final PackageRepository packages;
    private final UserRepository users;
    private final CompanyRepository companies;
    private final VnPayService vnPay;
    private final VnPayConfig config;

    public PaymentService(PaymentRepository payments, PackageRepository packages, UserRepository users,
                          CompanyRepository companies, VnPayService vnPay, VnPayConfig config) {
        this.payments = payments;
        this.packages = packages;
        this.users = users;
        this.companies = companies;
        this.vnPay = vnPay;
        this.config = config;
    }

    @Transactional
    public PaymentUrl createPayment(PaymentRequest request, Integer userId, String ipAddress) {
        User user = users.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        if (user.getRole() == null || !"RECRUIT".equals(user.getRole().getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only recruiters can purchase packages");
        }
        Company company = user.getCompany();
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has no company");
        }
        Package paymentPackage = packages.findById(request.getPackageId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found"));
        BigDecimal amount = paymentPackage.getPrice();
        if (amount == null || amount.signum() <= 0 || amount.stripTrailingZeros().scale() > 0
                || amount.compareTo(new BigDecimal("9999999999")) > 0
                || paymentPackage.getCredits() == null || paymentPackage.getCredits() < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Package has an invalid price or credits");
        }

        String txnRef = UUID.randomUUID().toString().replace("-", "");
        String url = vnPay.createPaymentUrl(txnRef, amount, request, ipAddress);
        LocalDateTime now = LocalDateTime.now();
        Payment payment = new Payment();
        payment.setTxnRef(txnRef);
        payment.setUser(user);
        payment.setCompany(company);
        payment.setPaymentPackage(paymentPackage);
        payment.setAmount(amount);
        payment.setCredits(paymentPackage.getCredits());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(now);
        payment.setUpdatedAt(now);
        payments.save(payment);
        return new PaymentUrl(txnRef, url);
    }

    @Transactional(readOnly = true)
    public PaymentDetails getPayment(String txnRef, Integer userId) {
        Payment payment = payments.findByTxnRefAndUserId(txnRef, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        return new PaymentDetails(payment.getTxnRef(), payment.getAmount(), payment.getCredits(),
                payment.getStatus(), payment.getVnpResponseCode(), payment.getVnpTransactionStatus(),
                payment.getCreatedAt(), payment.getPaidAt());
    }

    @Transactional
    public IpnResponse handleIpn(Map<String, String> fields) {
        if (!vnPay.hasValidSignature(fields)) {
            return new IpnResponse("97", "Invalid Checksum");
        }
        String txnRef = fields.get("vnp_TxnRef");
        String amount = fields.get("vnp_Amount");
        String responseCode = fields.get("vnp_ResponseCode");
        String transactionStatus = fields.get("vnp_TransactionStatus");
        String transactionNo = fields.get("vnp_TransactionNo");
        String bankCode = fields.get("vnp_BankCode");
        if (!config.getTmnCode().equals(fields.get("vnp_TmnCode"))
                || txnRef == null || txnRef.isBlank() || txnRef.length() > 100
                || amount == null || !amount.matches("[0-9]{1,12}")
                || responseCode == null || !responseCode.matches("[0-9]{2}")
                || transactionStatus == null || !transactionStatus.matches("[0-9]{2}")
                || (transactionNo != null && !transactionNo.matches("[0-9]{1,100}"))
                || (bankCode != null && !bankCode.matches("[A-Za-z0-9]{1,20}"))) {
            return new IpnResponse("99", "Invalid payment data");
        }
        boolean success = "00".equals(responseCode) && "00".equals(transactionStatus);
        if (success && (transactionNo == null || transactionNo.isEmpty())) {
            return new IpnResponse("99", "Missing transaction number");
        }
        Payment payment = payments.findByTxnRefForUpdate(txnRef).orElse(null);
        if (payment == null) {
            return new IpnResponse("01", "Order not found");
        }
        BigInteger expectedAmount = payment.getAmount().movePointRight(2).toBigIntegerExact();
        if (!expectedAmount.equals(new BigInteger(amount))) {
            return new IpnResponse("04", "Invalid amount");
        }
        if (payment.getStatus() != PaymentStatus.PENDING) {
            return new IpnResponse("02", "Order already confirmed");
        }
        if (success && companies.addCredits(payment.getCompany().getId(), payment.getCredits(),
                Integer.MAX_VALUE - payment.getCredits()) != 1) {
            throw new IllegalStateException("Cannot update company credits for payment " + txnRef);
        }
        LocalDateTime now = LocalDateTime.now();
        // VNPay code 07 can mean the customer was charged but the payment is suspicious.
        // Keep it pending for manual reconciliation rather than treating it as failed.
        payment.setStatus("07".equals(responseCode) ? PaymentStatus.PENDING
                : success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        payment.setVnpResponseCode(responseCode);
        payment.setVnpTransactionStatus(transactionStatus);
        payment.setVnpTransactionNo(transactionNo);
        payment.setBankCode(bankCode);
        payment.setPaidAt(success ? now : null);
        payment.setUpdatedAt(now);
        return new IpnResponse("00", "Confirm Success");
    }

    public record PaymentUrl(String txnRef, String paymentUrl) {}

    public record PaymentDetails(String txnRef, BigDecimal amount, Integer credits, PaymentStatus status,
                                 String vnpResponseCode, String vnpTransactionStatus,
                                 LocalDateTime createdAt, LocalDateTime paidAt) {}
}
