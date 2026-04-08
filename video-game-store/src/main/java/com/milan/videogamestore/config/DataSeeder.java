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

            Console ps4 = new Console();
            ps5.setName("PlayStation 4");
            ps5 = consoleRepository.save(ps4);

            Console xsx = new Console();
            xsx.setName("Xbox Series X|S");
            xsx = consoleRepository.save(xsx);

            Console xb360 = new Console();
            xsx.setName("Xbox 360");
            xsx = consoleRepository.save(xb360);

            Console switchConsole = new Console();
            switchConsole.setName("Nintendo Switch");
            switchConsole = consoleRepository.save(switchConsole);

            // Games
            Game g1 = new Game();
            g1.setName("Elden Ring");
            g1.setImageUrl("https://assets-prd.ignimgs.com/2021/06/12/elden-ring-button-03-1623460560664.jpg");
            g1.setGameUrl("https://en.bandainamcoent.eu/elden-ring/elden-ring");
            g1.setDescription("Elden Ring is a 2022 action role-playing game directed by Hidetaka Miyazaki with worldbuilding provided by the American fantasy writer George R. R. Martin. Developed by FromSoftware and published by Bandai Namco Entertainment, it was first released on February 25, 2022 for PlayStation 4, PlayStation 5, Windows, Xbox One and Xbox Series X/S. Set in the Lands Between, players control a customizable player character on a quest to repair the Elden Ring and become the new Elden Lord.");
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
            g1.setImageUrl("https://upload.wikimedia.org/wikipedia/en/c/c7/Helldivers_art.jpg");
            g1.setGameUrl("https://en.wikipedia.org/wiki/Helldivers");
            g2.setDescription("Helldivers (stylized in all caps) is a 2015 top-down shooter video game developed by Arrowhead Game Studios and published by Sony Computer Entertainment. The game was released for PlayStation 3, PlayStation 4, and PlayStation Vita (with cross-play) in March 2015.[4] A version for Windows was also released on 7 December 2015, making it the first Sony-published game for personal computers since Twisted Metal 2 and Jet Moto in 1997.[5] The story follows the Helldivers, a unit of shock troops mobilized to spread managed democracy and defend their way of life.");
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
            g1.setGameUrl("https://www.metacritic.com/game/baldurs-gate-3/");
            g3.setDescription("Baldur’s Gate 3 is a story-rich, party-based RPG set in the universe of Dungeons & Dragons, where your choices shape a tale of fellowship and betrayal, survival and sacrifice, and the lure of absolute power.");
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

            Game g9 = new Game();
            g9.setName("Buy Call of Duty®: Black Ops 7 - Vault Edition");
            g9.setDescription("The Call of Duty® experience supports Call of Duty®: Black Ops 7, Call of Duty®: Black Ops 6, and Call of Duty®: Warzone™.");
            g9.setImageUrl("https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1938090/06107605348820087bb51ca89ed620c22fe559aa/header.jpg?t=1775145994");
            g9.setGameUrl("https://store.steampowered.com/app/1938090/Call_of_Duty/");
            g9.setReleaseDate(OffsetDateTime.now().minusYears(4));
            g9.setPrice(new BigDecimal("31.67"));
            g9.setMetacriticScore(79.0);
            g9.setWonGameOfTheYearAward(false);
            g9.setGameType(multi);
            g9.setGameGenres(Set.of(action, shooter));
            g9.setConsoles(Set.of(pc, ps5, xsx, switchConsole));
            gameRepository.save(g9);

            Game g10 = new Game();
            g10.setName("Dark Souls I");
            g10.setDescription("Dark Souls[a] is a dark fantasy action role-playing game series developed by FromSoftware and published by Bandai Namco Entertainment. Created by Hidetaka Miyazaki, the series began with the release of Dark Souls (2011) and has seen two sequels, Dark Souls II (2014) and Dark Souls III (2016). It has received critical acclaim, with its high level of difficulty being among its most discussed aspects, while the first Dark Souls is often cited as one of the greatest games of all time. The series had shipped over 39 million copies worldwide as of 2025.[1] Other FromSoftware games, including Demon's Souls, Bloodborne, Sekiro: Shadows Die Twice, and Elden Ring, share several related concepts and led to the creation of the Soulslike subgenre.");
            g10.setImageUrl("https://shared.fastly.steamstatic.com/store_item_assets/steam/apps/570940/header.jpg?t=1764975651");
            g10.setGameUrl("https://store.steampowered.com/app/570940/DARK_SOULS_REMASTERED/");
            g10.setReleaseDate(OffsetDateTime.now().minusYears(4));
            g10.setPrice(new BigDecimal("31.67"));
            g10.setMetacriticScore(79.0);
            g10.setWonGameOfTheYearAward(false);
            g10.setGameType(multi);
            g10.setGameGenres(Set.of(action, shooter));
            g10.setConsoles(Set.of(pc, ps5, xsx, switchConsole));
            gameRepository.save(g10);

            Game g11 = new Game();
            g11.setName("Doom: Eternal");
            g11.setDescription("Doom Eternal is a 2020 first-person shooter game developed by id Software and published by Bethesda Softworks. The sequel to Doom (2016), and the seventh game in the Doom series, it was released for PlayStation 4, Stadia, Windows, and Xbox One on March 20, 2020, with a port for Nintendo Switch released on December 8, 2020, and versions for PlayStation 5 and Xbox Series X/S released on June 29, 2021.");
            g11.setImageUrl("https://upload.wikimedia.org/wikipedia/en/9/9d/Cover_Art_of_Doom_Eternal.png");
            g11.setGameUrl("https://en.wikipedia.org/wiki/Doom_Eternal");
            g11.setReleaseDate(OffsetDateTime.now().minusYears(4));
            g11.setPrice(new BigDecimal("31.67"));
            g11.setMetacriticScore(79.0);
            g11.setWonGameOfTheYearAward(false);
            g11.setGameType(multi);
            g11.setGameGenres(Set.of(action, shooter));
            g11.setConsoles(Set.of(pc, ps5, xsx, switchConsole));
            gameRepository.save(g11);

            Game g12 = new Game();
            g12.setName("Doom: The Dark Ages");
            g12.setDescription("Doom: The Dark Ages is a 2025 first-person shooter game developed by id Software and published by Bethesda Softworks. It is the eighth main entry in the Doom franchise, following Doom Eternal (2020). The game is set many years prior to Doom (2016) and follows the Doom Slayer's efforts to save humanity during a war against Hell.");
            g12.setImageUrl("https://upload.wikimedia.org/wikipedia/en/7/7f/DOOM%2C_The_Dark_Ages_Game_Cover.jpeg");
            g12.setGameUrl("https://en.wikipedia.org/wiki/Doom:_The_Dark_Ages");
            g12.setReleaseDate(OffsetDateTime.now().minusYears(1));
            g12.setPrice(new BigDecimal("31.67"));
            g12.setMetacriticScore(79.99);
            g12.setWonGameOfTheYearAward(false);
            g12.setGameType(multi);
            g12.setGameGenres(Set.of(action, shooter));
            g12.setConsoles(Set.of(pc, ps5, xsx, switchConsole));
            gameRepository.save(g12);

            Game g13 = new Game();
            g13.setName("Ghost of Tsushima DIRECTOR'S CUT");
            g13.setDescription("A storm is coming. Venture into the complete Ghost of Tsushima DIRECTOR’S CUT on PC; forge your own path through this open-world action adventure and uncover its hidden wonders. Brought to you by Sucker Punch Productions, Nixxes Software and PlayStation Studios.");
            g13.setImageUrl("https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2215430/header.jpg?t=1763409398");
            g13.setGameUrl("https://store.steampowered.com/app/2215430/Ghost_of_Tsushima_DIRECTORS_CUT/");
            g13.setReleaseDate(OffsetDateTime.now().minusYears(1));
            g13.setPrice(new BigDecimal("31.67"));
            g13.setMetacriticScore(79.99);
            g13.setWonGameOfTheYearAward(false);
            g13.setGameType(multi);
            g13.setGameGenres(Set.of(action, shooter));
            g13.setConsoles(Set.of(pc, ps5, xsx, switchConsole));
            gameRepository.save(g13);

            Game g14 = new Game();
            g14.setName("Marvel’s Spider-Man Remastered");
            g14.setDescription("In Marvel’s Spider-Man Remastered, the worlds of Peter Parker and Spider-Man collide in an original action-packed story. Play as an experienced Peter Parker, fighting big crime and iconic villains in Marvel’s New York. Web-swing through vibrant neighborhoods and defeat villains with epic takedowns.");
            g14.setImageUrl("https://cdn1.epicgames.com/offer/4bc43145bb8245a5b5cc9ea262ffbe0e/EGS_MarvelsSpiderManRemastered_InsomniacGamesNixxesSoftware_S2_1200x1600-76424286902489f4d9639ac9b735c2b2");
            g14.setGameUrl("https://store.steampowered.com/app/1817070/Marvels_SpiderMan_Remastered/");
            g14.setReleaseDate(OffsetDateTime.now().minusYears(1));
            g14.setPrice(new BigDecimal("31.67"));
            g14.setMetacriticScore(79.99);
            g14.setWonGameOfTheYearAward(false);
            g14.setGameType(multi);
            g14.setGameGenres(Set.of(action, shooter));
            g14.setConsoles(Set.of(pc, ps5, xsx, switchConsole));
            gameRepository.save(g14);

            Game g15 = new Game();
            g15.setName("Returnal");
            g15.setDescription("Returnal is a 2021 third-person shooter game developed by Housemarque and published by Sony Interactive Entertainment for the PlayStation 5. A port to Windows developed by Climax Studios was released on 15 February 2023. It follows Selene Vassos, an astronaut who lands on the planet Atropos in search of the mysterious \"White Shadow\" signal and finds herself trapped in a time loop. The gameplay combines bullet hell, third-person shooter and roguelike elements.[1]");
            g15.setImageUrl("https://upload.wikimedia.org/wikipedia/en/9/91/Returnal_cover_art.jpg");
            g15.setGameUrl("https://en.wikipedia.org/wiki/Returnal");
            g15.setReleaseDate(OffsetDateTime.now().minusYears(1));
            g15.setPrice(new BigDecimal("31.67"));
            g15.setMetacriticScore(79.99);
            g15.setWonGameOfTheYearAward(false);
            g15.setGameType(multi);
            g15.setGameGenres(Set.of(action, shooter));
            g15.setConsoles(Set.of(pc, ps5, xsx, switchConsole));
            gameRepository.save(g15);

            Game g16 = new Game();
            g16.setName("Sekiro™: Shadows Die Twice - GOTY Edition");
            g16.setDescription("Game of the Year - The Game Awards 2019 Best Action Game of 2019 - IGN Carve your own clever path to vengeance in the award winning adventure from developer FromSoftware, creators of Bloodborne and the Dark Souls series. Take Revenge. Restore Your Honor. Kill Ingeniously.");
            g16.setImageUrl("https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/814380/header.jpg?t=1762888662");
            g16.setGameUrl("https://store.steampowered.com/app/814380/Sekiro_Shadows_Die_Twice__GOTY_Edition/");
            g16.setReleaseDate(OffsetDateTime.now().minusYears(1));
            g16.setPrice(new BigDecimal("31.67"));
            g16.setMetacriticScore(79.99);
            g16.setWonGameOfTheYearAward(false);
            g16.setGameType(single);
            g16.setGameGenres(Set.of(action, shooter));
            g16.setConsoles(Set.of(pc, ps5, xsx, switchConsole));
            gameRepository.save(g16);

            Game g17 = new Game();
            g17.setName("Lords of the Fallen");
            g17.setDescription("A vast world awaits in the medieval, dark fantasy action-RPG, Lords of the Fallen. As a fabled Dark Crusader, embark on an epic quest to overthrow Adyr, the demon God. The final major update Version 2.5 is out now: features deadlier boss battles and all-new Veteran Mode.");
            g17.setImageUrl("https://shared.fastly.steamstatic.com/store_item_assets/steam/apps/1501750/c793d85dc133f22112c7283b79dd89e2c0aa58a4/header.jpg?t=1773254369");
            g17.setGameUrl("https://store.steampowered.com/app/1501750/Lords_of_the_Fallen/");
            g17.setReleaseDate(OffsetDateTime.now().minusYears(3));
            g17.setPrice(new BigDecimal("29.99"));
            g17.setMetacriticScore(68.99);
            g17.setWonGameOfTheYearAward(false);
            g17.setGameType(single);
            g17.setGameGenres(Set.of(action, shooter));
            g17.setConsoles(Set.of(pc, ps5, xsx, switchConsole));
            gameRepository.save(g17);

            Game g18 = new Game();
            g18.setName("God of War");
            g18.setDescription("His vengeance against the Gods of Olympus years behind him, Kratos now lives as a man in the realm of Norse Gods and monsters. It is in this harsh, unforgiving world that he must fight to survive… and teach his son to do the same.\n");
            g18.setImageUrl("https://shared.fastly.steamstatic.com/store_item_assets/steam/apps/1593500/header.jpg?t=1763059412");
            g18.setGameUrl("https://store.steampowered.com/app/1593500/God_of_War/");
            g18.setReleaseDate(OffsetDateTime.now().minusYears(1));
            g18.setPrice(new BigDecimal("49.67"));
            g18.setMetacriticScore(93.00);
            g18.setWonGameOfTheYearAward(false);
            g18.setGameType(single);
            g18.setGameGenres(Set.of(action, adventure));
            g18.setConsoles(Set.of(pc, ps5));
            gameRepository.save(g18);

            Game g19 = new Game();
            g19.setName("Fall Guys");
            g19.setDescription("Fall Guys is a free, cross-platform massively multiplayer party royale game. Clumsily compete in absurd obstacle courses with friends or build your very own chaotic course to share with the community.");
            g19.setImageUrl("https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1097150/header.jpg?t=1698763175");
            g19.setGameUrl("https://steamcommunity.com/app/1097150");
            g19.setReleaseDate(OffsetDateTime.now().minusYears(6));
            g19.setPrice(new BigDecimal("10.67"));
            g19.setMetacriticScore(83.45);
            g19.setWonGameOfTheYearAward(false);
            g19.setGameType(multi);
            g19.setGameGenres(Set.of(action, shooter));
            g19.setConsoles(Set.of(pc, ps5, xsx, switchConsole));
            gameRepository.save(g19);

            Game g20 = new Game();
            g20.setName("I Am Bread");
            g20.setDescription("You are bread. Your mission, become toast! Take on all hazards to deliciousness as you head on an epic adventure in one of the the toughest games ever baked. All of the game modes (including Bagel Race) are now unlocked so there's sure to be something to suit everyone's taste!");
            g20.setImageUrl("https://shared.fastly.steamstatic.com/store_item_assets/steam/apps/327890/header.jpg?t=1739399026");
            g20.setGameUrl("https://store.steampowered.com/app/327890/I_Am_Bread/");
            g20.setReleaseDate(OffsetDateTime.now().minusYears(7));
            g20.setPrice(new BigDecimal("12.79"));
            g20.setMetacriticScore(79.99);
            g20.setWonGameOfTheYearAward(false);
            g20.setGameType(single);
            g20.setGameGenres(Set.of(action, shooter));
            g20.setConsoles(Set.of(pc, ps5, ps4));
            gameRepository.save(g20);
        };
    }
}
