package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.game.Game;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    @EntityGraph(attributePaths = {"gameType", "gameGenres", "consoles", "reviews"})
    List<Game> findAll();

    @EntityGraph(attributePaths = {"gameType", "gameGenres", "consoles", "reviews"})
    Optional<Game> findById(Long id);
}
