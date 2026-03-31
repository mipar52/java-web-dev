package com.milan.videogamestore.model.dto.paypal;

import lombok.Data;

import java.util.List;

@Data
public class PayPalCreateOrderResponse {
    private String id;
    private List<Link> links;

    @Data
    public static class Link {
        private String href;
        private String rel;
        private String method;
    }
}
