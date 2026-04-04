package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.game.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    @EntityGraph(attributePaths = {"gameType", "gameGenres", "consoles"})
    List<Game> findAll();

    // 1) AND filter
    @Query("""
           select g.id
           from Game g
           join g.gameGenres gg
           where gg.id in :genreIds
           group by g.id
           having count(distinct gg.id) = :genreCount
           """)
    List<Long> findIdsByAllGenres(
            @Param("genreIds") Collection<Long> genreIds,
            @Param("genreCount") long genreCount
    );

    // 2) igre + relacije
    @EntityGraph(attributePaths = {"gameType", "gameGenres", "consoles"})
    @Query("""
           select distinct g
           from Game g
           where g.id in :ids
           """)
    List<Game> findAllByIdInWithGraph(@Param("ids") Collection<Long> ids);

    @EntityGraph(attributePaths = {"gameType", "gameGenres", "consoles"})
    @Query("""
       select g
       from Game g
       where (:q is null or :q = '' or lower(g.name) like lower(concat('%', :q, '%')))
       order by g.name asc
       """)
    List<Game> adminSearch(@Param("q") String q);

    @EntityGraph(attributePaths = {"gameType", "gameGenres", "consoles"})
    Page<Game> findAll(Pageable pageable);
}
