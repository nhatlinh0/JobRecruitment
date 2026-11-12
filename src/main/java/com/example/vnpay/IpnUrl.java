package com.example.vnpay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/vnpay")
public class IpnUrl {
    private static final Logger log = LoggerFactory.getLogger(IpnUrl.class);
    private final PaymentService service;

    public IpnUrl(PaymentService service) {
        this.service = service;
    }

    @GetMapping("/ipn")
    public IpnResponse handleIpn(@RequestParam Map<String, String> parameters) {
        try {
            return service.handleIpn(parameters);
        } catch (DataAccessException | IllegalStateException ex) {
            log.error("Failed to process VNPay IPN", ex);
            return new IpnResponse("99", "Payment processing error");
        }
    }

    public record IpnResponse(@JsonProperty("RspCode") String rspCode,
                              @JsonProperty("Message") String message) {}
}
