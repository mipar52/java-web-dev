package com.milan.videogamestore.model.dto;

import lombok.Data;

@Data
public class CheckoutForm {
    private String shippingAddress;
    private String city;
    private String zip;
    private String phone;
}
