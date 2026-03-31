package com.milan.videogamestore.controller;

import com.milan.videogamestore.model.orders.Order;
import com.milan.videogamestore.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;

    @GetMapping("/orders")
    public String myOrders(Authentication authentication, Model model) {
        List<Order> orders = orderRepository.findAllByUser_UsernameOrderByCreatedAtDesc(authentication.getName());
        model.addAttribute("orders", orders);
    return "order/list";
    }
}
