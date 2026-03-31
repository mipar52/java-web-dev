package com.milan.videogamestore.service.payment;

import com.milan.videogamestore.model.dto.paypal.PayPalAccessTokenResponse;
import com.milan.videogamestore.model.dto.paypal.PayPalCreateOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PayPalPaymentService implements PaymentService {
    private final RestClient restClient;

    @Value("${paypal.base-url}")
    private String baseUrl;

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.brand-name:Video Game Store}")
    private String brandName;


    @Override
    public String getAccessToken() {

        String basic = Base64.getEncoder().encodeToString(
                (clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)
        );

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");

        var response = restClient.post()
                .uri(baseUrl + "/v1/oauth2/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + basic)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(form)
                .retrieve()
                .body(PayPalAccessTokenResponse.class);

        if (response == null || response.getAccessToken() == null || response.getAccessToken().isBlank()) {
            throw new IllegalStateException("PayPal access token response was empty");
        }
        return response.getAccessToken();
    }

    @Override
    public CreatedOrder createOrder(BigDecimal total, String currency, String returnUrl, String cancelUrl) {
        String token = getAccessToken();

        String amount = total.setScale(2, RoundingMode.HALF_UP).toPlainString();
        var body = createBody(amount, total, currency, returnUrl, cancelUrl);
        var response = restClient.post()
                .uri(baseUrl + "/v2/checkout/orders")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(PayPalCreateOrderResponse.class);
        if (response == null || response.getId() == null) {
            throw new IllegalStateException("PayPal create order response was empty");
        }

        String approveUrl = response.getLinks().stream()
                .filter(link -> "approve".equalsIgnoreCase(link.getRel()))
                .findFirst()
                .map(PayPalCreateOrderResponse.Link::getHref)
                .orElseThrow(() -> new IllegalStateException("No approve link in PayPal response"));

        return new CreatedOrder(response.getId(), approveUrl);

    }

    @Override
    public void captureOrder(String orderId) {
        String token = getAccessToken();

        restClient.post()
                .uri(baseUrl + "/v2/checkout/orders/{id}/capture", orderId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Map.of())
                .retrieve()
                .toBodilessEntity();
    }

    private Map<String, Object> createBody(String amount, BigDecimal total, String currency, String returnUrl, String cancelUrl) {
        return Map.of(
                "intent", "CAPTURE",
                "purchase_units", new Object[] {
                        Map.of("amount", Map.of("currency_code", currency, "value", amount))
                },
                "application_context", Map.of("brand_name", brandName, "return_url",returnUrl, "cancel_url", cancelUrl)
        );
    }
}
