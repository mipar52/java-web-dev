package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.game.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {}
