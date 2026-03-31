package com.milan.videogamestore.controller;

import com.milan.videogamestore.model.cart.Cart;
import com.milan.videogamestore.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
@SessionAttributes("cart")
public class CartController {

    private final GameRepository gameRepository;

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @GetMapping
    public String viewCart(@ModelAttribute("cart") Cart cart, Model model) {
        var items = new ArrayList<CartItemView>();
        BigDecimal total = BigDecimal.ZERO;

        for (var e : cart.getQuantities().entrySet()) {
            var game = gameRepository.findById(e.getKey()).orElse(null);
            if (game == null) continue;

            int qty = e.getValue();
            var lineTotal = game.getPrice().multiply(BigDecimal.valueOf(qty));
            total = total.add(lineTotal);

            items.add(new CartItemView(game.getId(), game.getName(), game.getPrice(), qty, lineTotal));
        }

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "cart/view";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("gameId") Long gameId,
                            @RequestParam(value= "qty", defaultValue = "1") int qty,
                            @ModelAttribute("cart") Cart cart) {
        if (qty < 1) qty = 1;

        gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));
        cart.add(gameId, qty);
        return "redirect:/games/" + gameId + "?added=1";
    }

    @PostMapping("/items")
    public String addItem(
            @RequestParam Long gameId,
            @RequestParam(defaultValue = "1") int quantity,
            @RequestParam(required = false) String returnUrl,
            @ModelAttribute("cart") Cart cart
    ) {
        cart.add(gameId, quantity);

        if (returnUrl != null && !returnUrl.isBlank() && returnUrl.startsWith("/")) {
            return "redirect:" + returnUrl;
        }
        return "redirect:/games";
    }

    @PostMapping("/items/{gameId}")
    public String updateQuantity(
            @PathVariable Long gameId,
            @RequestParam int quantity,
            @ModelAttribute("cart") Cart cart
    ) {
        cart.setQuantity(gameId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/items/{gameId}/remove")
    public String removeItem(@PathVariable Long gameId, @ModelAttribute("cart") Cart cart) {
        cart.remove(gameId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clear(@ModelAttribute("cart") Cart cart) {
        cart.clear();
        return "redirect:/cart";
    }

    public record CartItemView(Long gameId, String name, BigDecimal price, int quantity, BigDecimal lineTotal) {}
}
