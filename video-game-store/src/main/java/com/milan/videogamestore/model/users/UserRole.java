package com.milan.videogamestore.model.users;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="user_roles")
@Data
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String id;

    public String name;
}
