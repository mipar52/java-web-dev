package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @Query("""
           select u
           from AppUser u
           join fetch u.role r
           where u.username = :username
           """)
    Optional<AppUser> findByUsernameWithRole(@Param("username") String username);
}