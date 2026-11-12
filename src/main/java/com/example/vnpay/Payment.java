package com.example.vnpay;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.example.security.UserDetailsCustom;
import com.example.service.PaymentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vnpay")
public class Payment {
    private final PaymentService service;

    public Payment(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/payments")
    public PaymentService.PaymentUrl createPayment(@Valid @RequestBody PaymentRequest body, HttpServletRequest request,
                                                   @AuthenticationPrincipal UserDetailsCustom principal) {
        return service.createPayment(body, principal.getUserEntity().getId(), request.getRemoteAddr());
    }

    @GetMapping("/payments/{txnRef}")
    public PaymentService.PaymentDetails getPayment(@PathVariable String txnRef,
                                                     @AuthenticationPrincipal UserDetailsCustom principal) {
        return service.getPayment(txnRef, principal.getUserEntity().getId());
    }
}
