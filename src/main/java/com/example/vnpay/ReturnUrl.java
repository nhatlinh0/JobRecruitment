package com.example.vnpay;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/vnpay")
public class ReturnUrl {
    private final VnPayService service;

    public ReturnUrl(VnPayService service) {
        this.service = service;
    }

    @GetMapping("/return")
    public ReturnResult handleReturn(@RequestParam Map<String, String> parameters) {
        if (!service.hasValidSignature(parameters)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid VNPay signature");
        }
        // The browser return is informational; only a verified IPN can settle an order.
        return new ReturnResult(parameters.get("vnp_TxnRef"), parameters.get("vnp_ResponseCode"),
                parameters.get("vnp_TransactionStatus"), "Check payment status with the application");
    }

    public record ReturnResult(String txnRef, String responseCode, String transactionStatus, String message) {}
}
