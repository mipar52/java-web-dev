package com.milan.videogamestore.repository;


import com.milan.videogamestore.model.orders.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"items", "items.game"})
    Optional<Order> findByIdAndUser_Username(Long id, String username);

    @EntityGraph(attributePaths = {"items", "items.game"})
    List<Order> findAllByUser_UsernameOrderByCreatedAtDesc(String username);

    @EntityGraph(attributePaths = {"items", "items.game"})
    Optional<Order> findByExternalPaymentIdAndUser_Username(String externalPaymentId, String username);

    @EntityGraph(attributePaths = {"user"})
    @Query("""
        select o
        from Order o
        where (:q is null or :q = '' or lower(o.user.username) like lower(concat('%', :q, '%')))
          and (:from is null or o.createdAt >= :from)
          and (:to is null or o.createdAt <= :to)
        order by o.createdAt desc
    """)
    List<Order> adminSearch(@Param("q") String q,
                            @Param("from") OffsetDateTime from,
                            @Param("to") OffsetDateTime to);

    @EntityGraph(attributePaths = {"user", "items", "items.game"})
    Optional<Order> findWithItemsById(Long id);
}
