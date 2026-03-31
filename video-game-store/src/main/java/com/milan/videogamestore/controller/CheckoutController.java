package com.milan.videogamestore.controller;

import com.milan.videogamestore.model.cart.Cart;
import com.milan.videogamestore.model.dto.CheckoutForm;
import com.milan.videogamestore.model.game.Game;
import com.milan.videogamestore.model.orders.Order;
import com.milan.videogamestore.model.orders.OrderItem;
import com.milan.videogamestore.model.orders.OrderStatus;
import com.milan.videogamestore.repository.AppUserRepository;
import com.milan.videogamestore.repository.GameRepository;
import com.milan.videogamestore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@SessionAttributes("cart")
public class CheckoutController {
    private final AppUserRepository appUserRepository;
    private final GameRepository gameRepository;
    private final OrderRepository orderRepository;

    @GetMapping("/checkout")
    public String checkout(@ModelAttribute("cart") Cart cart, Model model) {
        if (cart.getQuantities().isEmpty()) return "redirect:/cart";
        Summary summary = buildSummary(cart);

        model.addAttribute("items", summary.items());
        model.addAttribute("total", summary.total());
        model.addAttribute("form", new CheckoutForm());
        model.addAttribute("paymentMethod", "COD");

        return "checkout/view";
    }

    @PostMapping("/checkout")
    public String placeOrder(@ModelAttribute("cart") Cart cart, @ModelAttribute("form") CheckoutForm form, Authentication authentication) {
        if (cart.getQuantities().isEmpty()) return "redirect:/cart";

        var username = authentication.getName();
        var user = appUserRepository.findByUsernameWithRole(username).orElseThrow(() -> new IllegalArgumentException("Logged in user not found: " + username));

        Order order = new Order();
        order.setUser(user);

        order.setStatus(OrderStatus.CREATED);
        order.setPaymentMethod("COD");
        order.setShippingAddress(form.getShippingAddress());
        order.setCity(form.getCity());
        order.setZip(form.getZip());
        order.setPhone(form.getPhone());

        for (var e : cart.getQuantities().entrySet()) {
            var game = gameRepository.findById(e.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Game not found: " + e.getKey()));

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
        cart.clear();

        return "redirect:/orders/" + order.getId();
    }

    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable Long id, Authentication authentication, Model model) {
        Order order = orderRepository.findByIdAndUser_Username(id, authentication.getName()).orElseThrow(() -> new IllegalArgumentException("Order not found!"));

        BigDecimal total = order.getItems().stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("order", order);
        model.addAttribute("tota", total);
        return "order/details";
    }

    private Summary buildSummary(Cart cart) {
        var items = new ArrayList<CartLine>();

        BigDecimal total = BigDecimal.ZERO;

        for (var entrySet: cart.getQuantities().entrySet()) {
            Game game = gameRepository.findById(entrySet.getKey()).orElse(null);
            if (game == null) continue;

            int quantity = entrySet.getValue();
            var line = game.getPrice().multiply(BigDecimal.valueOf(quantity));
            total = total.add(line);
        }
        return new Summary(items, total);
    }

    public record CartLine(Long gameId, String name, int quantity, BigDecimal unitPrice, BigDecimal linePrice) {}
    public record Summary(ArrayList<CartLine> items, BigDecimal total) {}
}
