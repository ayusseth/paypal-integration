package com.ayush.paypal.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class HttpServiceEngine {
    private final RestClient restClient;        //our project is synchronous so we are using RestClient

    public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest) {
        //paypal giving response headers and body both so we might need ResponseEntity

        log.info("Making HTTP Call to PayPal OAuth API to retrieve access token in HttpServiceEngine");


        try {
            ResponseEntity<String> httpResponse = restClient
                    .method(httpRequest.getHttpMethod())
                    .uri(httpRequest.getUrl())

                    .headers(restClientHeader -> restClientHeader
                            .addAll(httpRequest.getHttpHeaders()))
                    .body(httpRequest.getBody())
                    .retrieve()
                    .toEntity(String.class);
            log.info("HTTP Response received from PayPal OAuth API: {}", httpResponse);
            return httpResponse;
        } catch (Exception e) {
            log.info("Exception occurred while making HTTP call to PayPal OAuth API: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve access token from PayPal OAuth API", e);
        }
    }
}



