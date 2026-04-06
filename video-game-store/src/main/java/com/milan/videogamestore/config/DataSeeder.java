package com.milan.videogamestore.config;

import com.milan.videogamestore.model.console.Console;
import com.milan.videogamestore.model.game.Game;
import com.milan.videogamestore.model.game.GameType;
import com.milan.videogamestore.model.game.Genre;
import com.milan.videogamestore.model.users.AppUser;
import com.milan.videogamestore.model.users.UserRole;
import com.milan.videogamestore.repository.*;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

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
            UserRoleRepository roleRepo,
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (gameRepository.count() > 0) {
                return;
            }

            UserRole userRole = roleRepo.findByName("USER")
                    .orElseGet(() -> roleRepo.save(new UserRole("USER")));

            UserRole adminRole = roleRepo.findByName("ADMIN")
                    .orElseGet(() -> roleRepo.save(new UserRole("ADMIN")));

            AppUser user = new AppUser();
            user.setFirstName("Adminka");
            user.setLastName("Adminkic");
            user.setEmail("admin@gmail.com");
            user.setUsername("admin");
            user.setPasswordHash(passwordEncoder.encode("admin"));
            user.setMobilePhone("0919284901");
            user.setRole(adminRole);

            AppUser newUser = appUserRepository.save(user);
            System.out.println("Added new user: " + newUser);

            GameType single = new GameType();
            single.setName("singleplayer");
            single = gameTypeRepository.save(single);


            GameType multi = new GameType();
            multi.setName("multiplayer");
            multi = gameTypeRepository.save(multi);

            GameType coop = new GameType();
            coop.setName("co-op");
            coop = gameTypeRepository.save(coop);

            // Genres
            Genre action = new Genre();
            action.setName("action");
            action = genreRepository.save(action);

            Genre adventure = new Genre();
            adventure.setName("adventure");
            adventure = genreRepository.save(adventure);

            Genre rpg = new Genre();
            rpg.setName("rpg");
            rpg = genreRepository.save(rpg);

            Genre soulslike = new Genre();
            soulslike.setName("soulslike");
            soulslike = genreRepository.save(soulslike);

            Genre shooter = new Genre();
            shooter.setName("shooter");
            shooter = genreRepository.save(shooter);

            Genre strategy = new Genre();
            strategy.setName("strategy");
            strategy = genreRepository.save(strategy);

            Genre simulation = new Genre();
            simulation.setName("simulation");
            simulation = genreRepository.save(simulation);

            Genre horror = new Genre();
            horror.setName("horror");
            horror = genreRepository.save(horror);

            // Consoles
            Console pc = new Console();
            pc.setName("PC");
            pc = consoleRepository.save(pc);

            Console ps5 = new Console();
            ps5.setName("PlayStation 5");
            ps5 = consoleRepository.save(ps5);

            Console xsx = new Console();
            xsx.setName("Xbox Series X|S");
            xsx = consoleRepository.save(xsx);

            Console switchConsole = new Console();
            switchConsole.setName("Nintendo Switch");
            switchConsole = consoleRepository.save(switchConsole);

            // Games
            Game g1 = new Game();
            g1.setName("Elden Ring");
            g1.setDescription("Open-world action RPG.");
            g1.setReleaseDate(OffsetDateTime.now().minusYears(4));
            g1.setPrice(new BigDecimal("59.99"));
            g1.setMetacriticScore(96.0);
            g1.setWonGameOfTheYearAward(true);
            g1.setGameType(single);
            g1.setGameGenres(Set.of(action, adventure, rpg, soulslike));
            g1.setConsoles(Set.of(pc, ps5, xsx));
            gameRepository.save(g1);

            Game g2 = new Game();
            g2.setName("Helldivers 2");
            g2.setDescription("Co-op PvE shooter.");
            g2.setReleaseDate(OffsetDateTime.now().minusYears(2));
            g2.setPrice(new BigDecimal("39.99"));
            g2.setMetacriticScore(82.0);
            g2.setWonGameOfTheYearAward(false);
            g2.setGameType(coop);
            g2.setGameGenres(Set.of(action, shooter));
            g2.setConsoles(Set.of(pc, ps5));
            gameRepository.save(g2);

            Game g3 = new Game();
            g3.setName("Baldur's Gate 3");
            g3.setDescription("Story-rich party-based RPG with turn-based combat.");
            g3.setReleaseDate(OffsetDateTime.now().minusYears(3));
            g3.setPrice(new BigDecimal("59.99"));
            g3.setMetacriticScore(96.0);
            g3.setWonGameOfTheYearAward(true);
            g3.setGameType(single);
            g3.setGameGenres(Set.of(rpg, adventure, strategy));
            g3.setConsoles(Set.of(pc, ps5, xsx));
            gameRepository.save(g3);

            Game g4 = new Game();
            g4.setName("Cyberpunk 2077");
            g4.setDescription("Futuristic open-world action RPG.");
            g4.setReleaseDate(OffsetDateTime.now().minusYears(5));
            g4.setPrice(new BigDecimal("49.99"));
            g4.setMetacriticScore(86.0);
            g4.setWonGameOfTheYearAward(false);
            g4.setGameType(single);
            g4.setGameGenres(Set.of(action, rpg, adventure));
            g4.setConsoles(Set.of(pc, ps5, xsx));
            gameRepository.save(g4);

            Game g5 = new Game();
            g5.setName("Hades");
            g5.setDescription("Fast-paced roguelike dungeon crawler.");
            g5.setReleaseDate(OffsetDateTime.now().minusYears(6));
            g5.setPrice(new BigDecimal("24.99"));
            g5.setMetacriticScore(93.0);
            g5.setWonGameOfTheYearAward(false);
            g5.setGameType(single);
            g5.setGameGenres(Set.of(action, adventure));
            g5.setConsoles(Set.of(pc, switchConsole, ps5, xsx));
            gameRepository.save(g5);

            Game g6 = new Game();
            g6.setName("Stardew Valley");
            g6.setDescription("Cozy farming & life simulation.");
            g6.setReleaseDate(OffsetDateTime.now().minusYears(10));
            g6.setPrice(new BigDecimal("14.99"));
            g6.setMetacriticScore(89.0);
            g6.setWonGameOfTheYearAward(false);
            g6.setGameType(single);
            g6.setGameGenres(Set.of(simulation, adventure));
            g6.setConsoles(Set.of(pc, switchConsole, ps5, xsx));
            gameRepository.save(g6);

            Game g7 = new Game();
            g7.setName("Resident Evil 4 (Remake)");
            g7.setDescription("Survival horror remake of a classic.");
            g7.setReleaseDate(OffsetDateTime.now().minusYears(3));
            g7.setPrice(new BigDecimal("59.99"));
            g7.setMetacriticScore(93.0);
            g7.setWonGameOfTheYearAward(false);
            g7.setGameType(single);
            g7.setGameGenres(Set.of(action, horror));
            g7.setConsoles(Set.of(pc, ps5, xsx));
            gameRepository.save(g7);

            Game g8 = new Game();
            g8.setName("Overwatch 2");
            g8.setDescription("Team-based multiplayer hero shooter.");
            g8.setReleaseDate(OffsetDateTime.now().minusYears(4));
            g8.setPrice(new BigDecimal("0.00"));
            g8.setMetacriticScore(79.0);
            g8.setWonGameOfTheYearAward(false);
            g8.setGameType(multi);
            g8.setGameGenres(Set.of(action, shooter));
            g8.setConsoles(Set.of(pc, ps5, xsx, switchConsole));
            gameRepository.save(g8);

            System.out.println("Seeded initial data (users, game types, genres, consoles, games).");
        };
    }
}
