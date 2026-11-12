package com.example.vnpay;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class VnPayService {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final VnPayConfig config;

    public VnPayService(VnPayConfig config) {
        this.config = config;
    }

    public String createPaymentUrl(String txnRef, BigDecimal amount, PaymentRequest request, String ipAddress) {
        requireConfigured(config.getTmnCode(), "VNPAY_TMN_CODE");
        requireConfigured(config.getHashSecret(), "VNPAY_HASH_SECRET");
        requireConfigured(config.getPayUrl(), "VNPAY_PAY_URL");
        requireConfigured(config.getReturnUrl(), "VNPAY_RETURN_URL");

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        Map<String, String> fields = new TreeMap<>();
        fields.put("vnp_Version", "2.1.0");
        fields.put("vnp_Command", "pay");
        fields.put("vnp_TmnCode", config.getTmnCode());
        fields.put("vnp_Amount", amount.multiply(BigDecimal.valueOf(100)).toBigIntegerExact().toString());
        fields.put("vnp_CurrCode", "VND");
        fields.put("vnp_TxnRef", txnRef);
        fields.put("vnp_OrderInfo", "Thanh toan goi " + request.getPackageId());
        fields.put("vnp_OrderType", "other");
        fields.put("vnp_Locale", request.getLanguage() == null ? "vn" : request.getLanguage());
        fields.put("vnp_ReturnUrl", config.getReturnUrl());
        fields.put("vnp_IpAddr", ipAddress);
        fields.put("vnp_CreateDate", DATE_FORMAT.format(now));
        fields.put("vnp_ExpireDate", DATE_FORMAT.format(now.plusMinutes(15)));
        if (request.getBankCode() != null && !request.getBankCode().isBlank()) {
            fields.put("vnp_BankCode", request.getBankCode());
        }

        String query = canonicalize(fields);
        return config.getPayUrl() + "?" + query + "&vnp_SecureHash=" + sign(query);
    }

    public boolean hasValidSignature(Map<String, String> parameters) {
        requireConfigured(config.getHashSecret(), "VNPAY_HASH_SECRET");
        String received = parameters.get("vnp_SecureHash");
        if (received == null || !received.matches("(?i)[0-9a-f]{128}")) {
            return false;
        }
        Map<String, String> fields = new TreeMap<>(parameters);
        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        byte[] expected = HexFormat.of().parseHex(sign(canonicalize(fields)));
        return MessageDigest.isEqual(expected, HexFormat.of().parseHex(received));
    }

    private String canonicalize(Map<String, String> fields) {
        return fields.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.US_ASCII);
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(config.getHashSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            return HexFormat.of().formatHex(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Cannot sign VNPay data", ex);
        }
    }

    private void requireConfigured(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, name + " is not configured");
        }
    }

}
