package com.milan.videogamestore.model.dto;

import com.milan.videogamestore.model.orders.PaymentMethod;
import lombok.Data;

@Data
public class CheckoutForm {
    private String shippingAddress;
    private String city;
    private String zip;
    private String phone;
    private PaymentMethod paymentMethod = PaymentMethod.COD;
}
