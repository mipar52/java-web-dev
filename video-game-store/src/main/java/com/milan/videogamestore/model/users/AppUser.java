package com.milan.videogamestore.model.users;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="app_users")
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String id;

    public String username;

    public String email;

    public String firstName;

    public String lastName;

    public String passwordHash;

    public String mobilePhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role_id")
    public UserRole role;
}
