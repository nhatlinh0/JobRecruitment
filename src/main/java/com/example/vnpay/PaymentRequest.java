package com.example.vnpay;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    @NotNull
    @Positive
    private Integer packageId;

    @Size(max = 20)
    @Pattern(regexp = "(?:[A-Za-z0-9]{3,20})?")
    private String bankCode;

    @Pattern(regexp = "vn|en")
    private String language;
}
