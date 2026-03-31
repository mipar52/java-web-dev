package com.milan.videogamestore.repository;


import com.milan.videogamestore.model.orders.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"items", "items.game"})
    Optional<Order> findByIdAndUser_Username(Long id, String username);

    @EntityGraph(attributePaths = {"items", "items.game"})
    List<Order> findAllByUser_UsernameOrderByCreatedAtDesc(String username);

    @EntityGraph(attributePaths = {"items", "items.game"})
    Optional<Order> findByExternalPaymentIdAndUser_Username(String externalPaymentId, String username);
}
