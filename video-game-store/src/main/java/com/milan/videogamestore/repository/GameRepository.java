package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
