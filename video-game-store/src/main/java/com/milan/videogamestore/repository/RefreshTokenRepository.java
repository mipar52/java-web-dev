package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> { }
