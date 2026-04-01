package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.game.Game;
import com.milan.videogamestore.model.game.Genre;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @EntityGraph(attributePaths = {"gameType", "gameGenres", "consoles"})
    @Query("""
           select g
           from Game g
           where (:q is null or :q = '' or lower(g.name) like lower(concat('%', :q, '%')))
           order by g.name asc
           """)
    List<Game> adminSearch(@Param("q") String q);
}
