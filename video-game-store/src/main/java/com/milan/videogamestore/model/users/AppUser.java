package com.milan.videogamestore.model.users;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="app_user")
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false, unique = true)
    public String username;

    @Column(nullable = false, unique = true)
    public String email;

    public String firstName;

    public String lastName;

    @Column(nullable = false)
    public String passwordHash;

    public String mobilePhone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_role_id", nullable = false)
    public UserRole role;
}
