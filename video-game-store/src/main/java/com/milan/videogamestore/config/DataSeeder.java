package com.milan.videogamestore.config;

import com.milan.videogamestore.model.console.Console;
import com.milan.videogamestore.model.game.Game;
import com.milan.videogamestore.model.game.GameType;
import com.milan.videogamestore.model.game.Genre;
import com.milan.videogamestore.model.users.AppUser;
import com.milan.videogamestore.model.users.UserRole;
import com.milan.videogamestore.repository.*;
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

            UserRole adminRole = roleRepo.findByName("ADMIN")
                    .orElseGet(() -> roleRepo.save(new UserRole("ADMIN")));

            UserRole userRole = roleRepo.findByName("USER")
                    .orElseGet(() -> roleRepo.save(new UserRole("USER")));

            AppUser adminUser = new AppUser();
            adminUser.setFirstName("Adminka");
            adminUser.setLastName("Adminkic");
            adminUser.setEmail("admin@gmail.com");
            adminUser.setUsername("admin");
            adminUser.setPasswordHash(passwordEncoder.encode("admin"));
            adminUser.setMobilePhone("0919284901");
            adminUser.setRole(adminRole);

            appUserRepository.save(adminUser);

            AppUser regularUser = new AppUser();
            regularUser.setFirstName("Branko");
            regularUser.setLastName("Kockica");
            regularUser.setEmail("branko.kockica@gmail.com");
            regularUser.setUsername("branko");
            regularUser.setPasswordHash(passwordEncoder.encode("branko"));
            regularUser.setMobilePhone("0919284901");
            regularUser.setRole(userRole);

            appUserRepository.save(adminUser);

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
            g1.setImageUrl("https://image.api.playstation.com/vulcan/ap/rnd/202110/2000/YMUoJUYNX0xWk6eTKuZLr5Iw.jpg");
            g1.setGameUrl("https://store.steampowered.com/app/1245620/ELDEN_RING/");
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
            g1.setImageUrl("https://gaming-cdn.com/images/products/9575/orig/helldivers-2-pc-game-steam-europe-and-us-and-canada-cover.jpg?v=1732563825");
            g1.setGameUrl("https://store.steampowered.com/app/553850/HELLDIVERS_2/");
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
            g1.setImageUrl("https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1086940/48a2fcbda8565bb45025e98fd8ebde8a7203f6a0/header.jpg?t=1773079016");
            g1.setGameUrl("https://store.steampowered.com/app/1086940/Baldurs_Gate_3/");
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
            g1.setImageUrl("https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1091500/e9047d8ec47ae3d94bb8b464fb0fc9e9972b4ac7/header.jpg?t=1769690377");
            g1.setGameUrl("https://store.steampowered.com/app/1091500/Cyberpunk_2077/");
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
            g1.setImageUrl("https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1145360/header.jpg?t=1758127023");
            g1.setGameUrl("https://store.steampowered.com/app/1145360/Hades/");
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
            g1.setImageUrl("https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/413150/header.jpg?t=1754692865");
            g1.setGameUrl("https://store.steampowered.com/app/413150/Stardew_Valley/");
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
            g1.setImageUrl("https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2050650/header.jpg?t=1772502922");
            g1.setGameUrl("https://store.steampowered.com/app/2050650/Resident_Evil_4/");
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
            g1.setImageUrl("https://upload.wikimedia.org/wikipedia/en/8/89/Overwatch_2_Steam_artwork.jpg");
            g1.setGameUrl("https://en.wikipedia.org/wiki/Overwatch_2");
            g8.setDescription("Team-based multiplayer hero shooter.");
            g8.setReleaseDate(OffsetDateTime.now().minusYears(4));
            g8.setPrice(new BigDecimal("31.67"));
            g8.setMetacriticScore(79.0);
            g8.setWonGameOfTheYearAward(false);
            g8.setGameType(multi);
            g8.setGameGenres(Set.of(action, shooter));
            g8.setConsoles(Set.of(pc, ps5, xsx, switchConsole));
            gameRepository.save(g8);
        };
    }
}
