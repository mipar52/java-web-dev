package com.milan.videogamestore.service.payment;

import java.math.BigDecimal;

public interface PaymentService {
    public String getAccessToken();
    public CreatedOrder createOrder(BigDecimal total, String currency, String returnUrl, String cancelUrl);
    public void captureOrder(String orderId);
    
    public record CreatedOrder(String id, String approveUrl) {}

}
