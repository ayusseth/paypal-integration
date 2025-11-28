package com.ayush.paypal.service.impl;

import com.ayush.paypal.service.TokenService;
import com.ayush.paypal.service.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final TokenService tokenService;


    @Override
    public String createOrder() {
        log.info("creating token..");
        String accessToken = tokenService.getAccessToken();
        log.info("token is created..");
        return "Token is retrieved : " + accessToken;
    }
}
