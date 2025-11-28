package com.ayush.paypal.service;

import com.ayush.paypal.constant.Constant;
import com.ayush.paypal.http.HttpRequest;
import com.ayush.paypal.http.HttpServiceEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final HttpServiceEngine httpServiceEngine;

    //TODO implement redis caching for access token and maintain expiry


    private static String accessToken;   //right now placing the token here, making it static so that it persists across multiple calls

    @Value("${paypal.oauth.url}")
    private String oauthUrl;

    @Value("${paypal.client.id}")
    private String clientID;  //redacted for security

    @Value("${paypal.client.secret}")
    private String clientSecret;  //redacted for security

    public String getAccessToken() {
        log.info("Retrieving Access token ");
        if (accessToken != null) {
            log.info("Access token is present in the configuration");
            return accessToken;
        }
        log.info("Access token is not present in the configuration, OAuth call to be made");


        HttpHeaders headers = new HttpHeaders();


        headers.setBasicAuth(clientID, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(Constant.GRANT_TYPE, Constant.CLIENT_CREDENTIALS);  //accessing constant values from Constant class


        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setHttpMethod(HttpMethod.POST);
        httpRequest.setUrl(oauthUrl);
        httpRequest.setHttpHeaders(headers);
        httpRequest.setBody(formData);

        ResponseEntity<String> response = httpServiceEngine.makeHttpCall(httpRequest);
        log.info("Http response from HttpServiceEngine {}", response);
        String tokenBody = response.getBody();
        return tokenBody;
    }
}
