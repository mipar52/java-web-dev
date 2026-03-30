package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> { }
