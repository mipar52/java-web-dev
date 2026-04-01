package com.milan.videogamestore.controller.admin;

import com.milan.videogamestore.model.game.Game;
import com.milan.videogamestore.model.orders.Order;
import com.milan.videogamestore.model.orders.OrderStatus;
import com.milan.videogamestore.model.users.AppUser;
import com.milan.videogamestore.model.users.UserRole;
import com.milan.videogamestore.repository.AppUserRepository;
import com.milan.videogamestore.repository.GameRepository;
import com.milan.videogamestore.repository.OrderRepository;
import com.milan.videogamestore.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository roleRepository;
    private final AppUserRepository appUserRepository;
    private final GameRepository gameRepository;

    @GetMapping("/admin/create")
    public String createAdmin() {
        AppUser user = new AppUser();
        user.setFirstName("Adminka");
        user.setLastName("Adminkic");
        user.setEmail("admin@gmail.com");
        user.setUsername("admin");
        user.setPasswordHash(passwordEncoder.encode("admin"));
        user.setMobilePhone("0919284901");

        UserRole role = roleRepository.findByName("ADMIN").orElseThrow();

        user.setRole(role);
        appUserRepository.save(user);
        return "";
    }

    @GetMapping("/admin/orders")
    public String orders(
            @RequestParam(required = false) String q,
            @RequestParam(required = false)LocalDate from,
            @RequestParam(required = false)LocalDate to,
            Model model
            ) {

        OffsetDateTime fromTs = from == null ? null : from.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime toTs = to == null ? null : to.atStartOfDay().atOffset(ZoneOffset.UTC);
        List<Order> orderList = orderRepository.adminSearch(q, fromTs, toTs);

        model.addAttribute("orders", orderList);
        model.addAttribute("q", q);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "admin/orders";
    }

    @PostMapping("admin/orders/{orderId}/status")
    public String updateStatus(@PathVariable Long orderId, @RequestParam("status") OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        order.setStatus(status);

        // slucaj ako se stavi da je korisnik platio
        // da se odmah stavi da je placeno po trenutnom datumu
        if (status == OrderStatus.PAID && order.getPaidAt() == null) {
            order.setPaidAt(OffsetDateTime.now());
        }

        if (order.getStatus() == OrderStatus.CANCELLED_BY_USER && status != OrderStatus.CANCELLED_BY_USER) {
         //   throw new IllegalStateException("Cannot change a user-cancelled order");
        }
        if (status == OrderStatus.PAID && order.getPaidAt() == null) {
            //   throw new IllegalStateException("Cannot set PAID without paidAt");
        }

        orderRepository.save(order);
        return "redirect:/admin/orders/" + order.getId();
    }

    @GetMapping("admin/orders/{id}")
    public String getOrderDetails(@PathVariable Long id, Model model) {

        var order = orderRepository.findWithItemsById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

        model.addAttribute("order", order);
        return "admin/order-details";
    }

    @PostMapping("/admin/orders/{id}/shipping")
    public String updateShipping(@PathVariable Long id,
                                 @RequestParam("shippingAddress") String shippingAddress,
                                 @RequestParam("city") String city,
                                 @RequestParam("zip") String zip,
                                 @RequestParam("phone") String phone) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

        order.setShippingAddress(shippingAddress == null ? "" : shippingAddress.trim());
        order.setCity(city == null ? "" : city.trim());
        order.setZip(zip == null ? "" : zip.trim());
        order.setPhone(phone == null ? "" : phone.trim());

        orderRepository.save(order);

        return "redirect:/admin/orders/" + order.getId();
    }
}
