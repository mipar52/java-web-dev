package com.milan.videogamestore.controller;

import com.milan.videogamestore.model.cart.Cart;
import com.milan.videogamestore.model.dto.CheckoutForm;
import com.milan.videogamestore.model.orders.Order;
import com.milan.videogamestore.model.orders.OrderItem;
import com.milan.videogamestore.model.orders.OrderStatus;
import com.milan.videogamestore.model.orders.PaymentMethod;
import com.milan.videogamestore.model.users.AppUser;
import com.milan.videogamestore.repository.AppUserRepository;
import com.milan.videogamestore.repository.GameRepository;
import com.milan.videogamestore.repository.OrderRepository;
import com.milan.videogamestore.service.payment.PaymentService;
import lombok.AllArgsConstructor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Controller
@RequiredArgsConstructor
@SessionAttributes("cart")
public class ExternalPaymentController {

    private final AppUserRepository appUserRepository;
    private final GameRepository gameRepository;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    @Value("${paypal.return-url}")
    private String payPalReturnUrl;

    @Value("${paypal.cancel-url}")
    private String getPayPalCanelUrl;

    @Value("${paypal-currency:EUR}")
    private String payPalCurrency;

    @PostMapping("/checkout/paypal/start")
    public String startPayPal(@ModelAttribute("cart") Cart cart, @ModelAttribute("form") CheckoutForm checkoutForm, Authentication authentication) {
        if (cart.getQuantities().isEmpty()) return "redirect:/cart";

        String username = authentication.getName();
        AppUser user = appUserRepository.findByUsernameWithRole(username).orElseThrow(() -> new IllegalStateException("Logged in user not found: " + username));

        BigDecimal total = BigDecimal.ZERO;
        for (var e : cart.getQuantities().entrySet()) {
            var game = gameRepository.findById(e.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Game not found: " + e.getKey()));
            total = total.add(game.getPrice().multiply(BigDecimal.valueOf(e.getValue())));
        }

        var order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PAYMENT_PENDING);
        order.setPaymentMethod(PaymentMethod.PAYPAL);

        order.setShippingAddress(checkoutForm.getShippingAddress());
        order.setCity(checkoutForm.getCity());
        order.setZip(checkoutForm.getZip());
        order.setPhone(checkoutForm.getPhone());

        for (var e : cart.getQuantities().entrySet()) {
            var game = gameRepository.findById(e.getKey()).orElseThrow();
            int qty = e.getValue();

            var item = new OrderItem();
            item.setOrder(order);
            item.setGame(game);
            item.setQuantity(qty);
            item.setUnitPrice(game.getPrice());
            item.setLineTotal(game.getPrice().multiply(BigDecimal.valueOf(qty)));
            order.getItems().add(item);
        }

        orderRepository.save(order);

        var created = paymentService.createOrder(total, payPalCurrency, payPalReturnUrl, getPayPalCanelUrl);
        order.setExternalPaymentId(created.id());
        orderRepository.save(order);

        return "redirect:" + created.approveUrl();
    }

    @GetMapping("/checkout/paypal/success")
    public String payPalSuccess(@RequestParam("token") String token, @ModelAttribute("cart") Cart cart, Authentication auth) {
        var order = orderRepository.findByExternalPaymentIdAndUser_Username(token, auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Order not found for PayPal token"));

        paymentService.captureOrder(token);

        order.setStatus(OrderStatus.PAID);
        order.setPaidAt(OffsetDateTime.now());
        orderRepository.save(order);

        cart.clear();

        return "redirect:/orders/" + order.getId();
    }

    @GetMapping("/checkout/paypal/cancel")
    public String payPalCancel(@RequestParam(value = "token", required = false) String token, Authentication auth) {
        if (token != null && !token.isBlank()) {
            orderRepository.findByExternalPaymentIdAndUser_Username(token, auth.getName())
                    .ifPresent(o -> {
                        o.setStatus(OrderStatus.CANCELLED_BY_USER);
                        orderRepository.save(o);
                    });
        }
        return "redirect:/cart";
    }


}
