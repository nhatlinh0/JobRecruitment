package com.example.vnpay;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;
import com.example.service.PaymentService;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VnPayTest {
    private static final String SIGNATURE =
            "d7a9255f6183cfcf9b37ecf316c0f58a613c9739180acaab52b736710cbd0618"
                    + "02382fa57ba120fd5238c2ed4c419f3ab45deda7e28af9818a99370415c641a7";
    private final VnPayService service = new VnPayService(new VnPayConfig(
            "TESTCODE", "test-secret", "https://sandbox.example/pay", "https://example.com/return"));

    @Test
    void createsSignedPaymentUrlWithCorrectAmount() {
        PaymentRequest request = new PaymentRequest();
        request.setPackageId(1);
        String txnRef = "test123";
        String url = service.createPaymentUrl(txnRef, new BigDecimal("10000"), request, "127.0.0.1");

        assertTrue(url.startsWith("https://sandbox.example/pay?"));
        Map<String, String> parameters = Arrays.stream(url.split("\\?", 2)[1].split("&"))
                .map(item -> item.split("=", 2))
                .collect(Collectors.toMap(item -> URLDecoder.decode(item[0], StandardCharsets.US_ASCII),
                        item -> URLDecoder.decode(item[1], StandardCharsets.US_ASCII)));
        assertEquals("1000000", parameters.get("vnp_Amount"));
        assertEquals(txnRef, parameters.get("vnp_TxnRef"));
        assertEquals("Thanh toan goi 1", parameters.get("vnp_OrderInfo"));
        assertEquals("vn", parameters.get("vnp_Locale"));
        assertTrue(service.hasValidSignature(parameters));
        parameters.put("vnp_Amount", "1");
        assertFalse(service.hasValidSignature(parameters));
    }

    @Test
    void validatesKnownSignatureAndIgnoresSignatureType() {
        Map<String, String> fields = Map.of("vnp_TxnRef", "ABC", "vnp_Amount", "10000",
                "vnp_SecureHash", SIGNATURE, "vnp_SecureHashType", "HmacSHA512");
        assertTrue(service.hasValidSignature(fields));
        assertFalse(service.hasValidSignature(Map.of("vnp_SecureHash", "bad")));
    }

    @Test
    void missingCredentialsAreReported() {
        VnPayService unconfigured = new VnPayService(new VnPayConfig("", "", "", ""));
        assertThrows(ResponseStatusException.class, () ->
                unconfigured.createPaymentUrl("test", BigDecimal.ONE, new PaymentRequest(), "127.0.0.1"));
        assertThrows(ResponseStatusException.class, () -> unconfigured.hasValidSignature(Map.of()));
    }

    @Test
    void returnUrlValidatesSignature() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new ReturnUrl(service)).build();
        mvc.perform(get("/api/v1/vnpay/return").param("vnp_SecureHash", "bad"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/v1/vnpay/return")
                        .param("vnp_TxnRef", "ABC").param("vnp_Amount", "10000").param("vnp_SecureHash", SIGNATURE))
                .andExpect(status().isOk()).andExpect(jsonPath("$.txnRef").value("ABC"))
                .andExpect(jsonPath("$.message").value("Check payment status with the application"));
    }

    @Test
    void rejectsInvalidPaymentRequests() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new Payment(mock(PaymentService.class))).build();
        mvc.perform(post("/api/v1/vnpay/payments").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"packageId\": 0}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/v1/vnpay/payments").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bankCode\": \"a\"}"))
                .andExpect(status().isBadRequest());
    }
}
