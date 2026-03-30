package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.console.Console;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsoleRepository extends JpaRepository<Console, Long> { }
