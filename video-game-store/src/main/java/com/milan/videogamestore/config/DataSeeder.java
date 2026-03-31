package com.milan.videogamestore.config;

import com.milan.videogamestore.model.console.Console;
import com.milan.videogamestore.model.game.Game;
import com.milan.videogamestore.model.game.GameType;
import com.milan.videogamestore.model.game.Genre;
import com.milan.videogamestore.model.users.UserRole;
import com.milan.videogamestore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {
    @Bean
    CommandLineRunner seedData(
            GameRepository gameRepository,
            GameTypeRepository gameTypeRepository,
            GenreRepository genreRepository,
            ConsoleRepository consoleRepository,
            UserRoleRepository roleRepo
    ) {
        return args -> {
            if (gameRepository.count() > 0) {
                return;
            }

            UserRole userRole = roleRepo.findByName("USER")
                    .orElseGet(() -> roleRepo.save(new UserRole("USER")));

            UserRole adminRole = roleRepo.findByName("ADMIN")
                    .orElseGet(() -> roleRepo.save(new UserRole("ADMIN")));

            GameType single = new GameType();
            single.setName("singleplayer");
            single = gameTypeRepository.save(single);

            GameType multi = new GameType();
            multi.setName("multiplayer");
            multi = gameTypeRepository.save(multi);

            Genre action = new Genre();
            action.setName("action");
            action = genreRepository.save(action);

            Genre adventure = new Genre();
            adventure.setName("adventure");
            adventure = genreRepository.save(adventure);

            Console pc = new Console();
            pc.setName("PC");
            pc = consoleRepository.save(pc);

            Console ps5 = new Console();
            ps5.setName("PlayStation 5");
            ps5 = consoleRepository.save(ps5);

            Game g1 = new Game();
            g1.setName("Elden Ring");
            g1.setDescription("Open-world action RPG.");
            g1.setReleaseDate(OffsetDateTime.now().minusYears(4));
            g1.setPrice(new BigDecimal("59.99"));
            g1.setMetacriticScore(96.0);
            g1.setWonGameOfTheYearAward(true);
            g1.setGameType(single);
            g1.setGameGenres(Set.of(action, adventure));
            g1.setConsoles(Set.of(pc, ps5));
            gameRepository.save(g1);

            Game g2 = new Game();
            g2.setName("Helldivers 2");
            g2.setDescription("Co-op PvE shooter.");
            g2.setReleaseDate(OffsetDateTime.now().minusYears(2));
            g2.setPrice(new BigDecimal("39.99"));
            g2.setMetacriticScore(82.0);
            g2.setGameType(multi);
            g2.setGameGenres(Set.of(action));
            g2.setConsoles(Set.of(pc, ps5));
            gameRepository.save(g2);
        };
    }
}
