package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.users.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository  extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(String name);
}
