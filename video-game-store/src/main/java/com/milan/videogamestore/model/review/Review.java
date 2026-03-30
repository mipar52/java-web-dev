package com.milan.videogamestore.model.review;

import com.milan.videogamestore.model.game.Game;
import com.milan.videogamestore.model.users.AppUser;
import lombok.Data;
import jakarta.persistence.*;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "reviews")
@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 2000)
    private String comment;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser user;
}