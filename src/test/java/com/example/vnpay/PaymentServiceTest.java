package com.example.vnpay;

import com.example.entity.Company;
import com.example.entity.Package;
import com.example.entity.Payment;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.enums.PaymentStatus;
import com.example.repository.CompanyRepository;
import com.example.repository.PackageRepository;
import com.example.repository.PaymentRepository;
import com.example.repository.UserRepository;
import com.example.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {
    private final PaymentRepository payments = mock(PaymentRepository.class);
    private final PackageRepository packages = mock(PackageRepository.class);
    private final UserRepository users = mock(UserRepository.class);
    private final CompanyRepository companies = mock(CompanyRepository.class);
    private final VnPayService vnPay = mock(VnPayService.class);
    private final VnPayConfig config = new VnPayConfig("TESTCODE", "secret", "url", "return");
    private final PaymentService service = new PaymentService(payments, packages, users, companies, vnPay, config);

    @Test
    void creationSnapshotsPackageAndCompanyForRecruiter() {
        Company company = new Company();
        company.setId(7);
        Role role = new Role();
        role.setName("RECRUIT");
        User user = new User();
        user.setId(2);
        user.setRole(role);
        user.setCompany(company);
        Package item = new Package();
        item.setPrice(new BigDecimal("10000.00"));
        item.setCredits(5);
        PaymentRequest request = new PaymentRequest();
        request.setPackageId(3);
        when(users.findById(2)).thenReturn(Optional.of(user));
        when(packages.findById(3)).thenReturn(Optional.of(item));
        when(vnPay.createPaymentUrl(anyString(), eq(item.getPrice()), eq(request), anyString())).thenReturn("signed-url");

        PaymentService.PaymentUrl result = service.createPayment(request, 2, "127.0.0.1");
        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(payments).save(captor.capture());
        Payment payment = captor.getValue();
        assertEquals(result.txnRef(), payment.getTxnRef());
        assertEquals("signed-url", result.paymentUrl());
        assertSame(company, payment.getCompany());
        assertEquals(item.getPrice(), payment.getAmount());
        assertEquals(5, payment.getCredits());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    void rejectsNonRecruitersAndUsersWithoutCompany() {
        User user = new User();
        Role role = new Role();
        role.setName("USER");
        user.setRole(role);
        when(users.findById(1)).thenReturn(Optional.of(user));
        assertEquals(403, assertThrows(ResponseStatusException.class,
                () -> service.createPayment(new PaymentRequest(), 1, "ip")).getStatusCode().value());
        role.setName("RECRUIT");
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.createPayment(new PaymentRequest(), 1, "ip")).getStatusCode().value());
        verifyNoInteractions(packages, payments, companies);
    }

    @Test
    void successfulIpnCreditsSnapshotCompanyOnlyOnce() {
        Payment payment = pendingPayment();
        Map<String, String> fields = validFields();
        when(vnPay.hasValidSignature(fields)).thenReturn(true);
        when(payments.findByTxnRefForUpdate("ref")).thenReturn(Optional.of(payment));
        when(companies.addCredits(7, 5, Integer.MAX_VALUE - 5)).thenReturn(1);

        assertEquals("00", service.handleIpn(fields).rspCode());
        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
        assertNotNull(payment.getPaidAt());
        assertEquals("02", service.handleIpn(fields).rspCode());
        verify(companies, times(1)).addCredits(7, 5, Integer.MAX_VALUE - 5);
    }

    @Test
    void failedOrInvalidIpnsNeverCreditCompany() {
        Map<String, String> fields = validFields();
        assertEquals("97", service.handleIpn(fields).rspCode());
        when(vnPay.hasValidSignature(fields)).thenReturn(true);
        when(payments.findByTxnRefForUpdate("ref")).thenReturn(Optional.empty());
        assertEquals("01", service.handleIpn(fields).rspCode());
        Payment payment = pendingPayment();
        when(payments.findByTxnRefForUpdate("ref")).thenReturn(Optional.of(payment));
        fields.put("vnp_Amount", "1");
        assertEquals("04", service.handleIpn(fields).rspCode());
        fields.put("vnp_Amount", "1000000");
        fields.put("vnp_TransactionStatus", "02");
        assertEquals("00", service.handleIpn(fields).rspCode());
        assertEquals(PaymentStatus.FAILED, payment.getStatus());
        assertNull(payment.getPaidAt());
        verifyNoInteractions(companies);
    }

    @Test
    void creditUpdateFailureDoesNotMarkPaymentSuccessful() {
        Payment payment = pendingPayment();
        Map<String, String> fields = validFields();
        when(vnPay.hasValidSignature(fields)).thenReturn(true);
        when(payments.findByTxnRefForUpdate("ref")).thenReturn(Optional.of(payment));
        assertThrows(IllegalStateException.class, () -> service.handleIpn(fields));
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    void suspiciousChargedTransactionRemainsPendingForReview() {
        Payment payment = pendingPayment();
        Map<String, String> fields = validFields();
        fields.put("vnp_ResponseCode", "07");
        when(vnPay.hasValidSignature(fields)).thenReturn(true);
        when(payments.findByTxnRefForUpdate("ref")).thenReturn(Optional.of(payment));
        assertEquals("00", service.handleIpn(fields).rspCode());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        assertEquals("07", payment.getVnpResponseCode());
        verifyNoInteractions(companies);
    }

    @Test
    void statusIsRestrictedToThePurchaser() {
        when(payments.findByTxnRefAndUserId("ref", 2)).thenReturn(Optional.empty());
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.getPayment("ref", 2)).getStatusCode().value());
    }

    private Payment pendingPayment() {
        Company company = new Company();
        company.setId(7);
        Payment payment = new Payment();
        payment.setCompany(company);
        payment.setAmount(new BigDecimal("10000.00"));
        payment.setCredits(5);
        payment.setStatus(PaymentStatus.PENDING);
        return payment;
    }

    private Map<String, String> validFields() {
        Map<String, String> fields = new HashMap<>();
        fields.put("vnp_TmnCode", "TESTCODE");
        fields.put("vnp_TxnRef", "ref");
        fields.put("vnp_Amount", "1000000");
        fields.put("vnp_ResponseCode", "00");
        fields.put("vnp_TransactionStatus", "00");
        fields.put("vnp_TransactionNo", "123");
        return fields;
    }
}
