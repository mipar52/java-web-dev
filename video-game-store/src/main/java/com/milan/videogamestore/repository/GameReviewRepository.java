package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.review.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GameReviewRepository extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<Review> findAllByGame_IdOrderByCreatedAtDesc(Long gameId);

    Optional<Review> findByGame_IdAndUser_Username(Long gameId, String username);

    @Query("""
           select avg(r.rating)
           from Review r
           where r.game.id = :gameId and r.rating is not null
           """)
    Optional<Double> findAverageRatingByGameId(@Param("gameId") Long gameId);

    @Query("""
           select count(r)
           from Review r
           where r.game.id = :gameId and r.rating is not null
           """)
    long countRatingsByGameId(@Param("gameId") Long gameId);
}
