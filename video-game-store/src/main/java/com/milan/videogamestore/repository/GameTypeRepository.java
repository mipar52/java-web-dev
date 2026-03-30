package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.game.GameType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameTypeRepository extends JpaRepository<GameType, Long> { }
