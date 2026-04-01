package com.milan.videogamestore.model.game;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "game_cover")
@Data
public class GameCover {
    @Id
    private Long gameId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(nullable = false, length = 100)
    private String contentType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private byte[] data;
}
