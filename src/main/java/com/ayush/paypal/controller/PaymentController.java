package com.ayush.paypal.controller;

import com.ayush.paypal.http.HttpServiceEngine;
import com.ayush.paypal.service.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final HttpServiceEngine httpServiceEngine;


    @PostMapping("/payments")
    public String createOrder() {
        log.info("getting request to get access token");
        return paymentService.createOrder();
    }
}
