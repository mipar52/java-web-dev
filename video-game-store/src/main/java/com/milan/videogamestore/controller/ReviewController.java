package com.milan.videogamestore.controller;

import com.milan.videogamestore.model.dto.ReviewForm;
import com.milan.videogamestore.model.game.Game;
import com.milan.videogamestore.model.review.Review;
import com.milan.videogamestore.model.users.AppUser;
import com.milan.videogamestore.repository.AppUserRepository;
import com.milan.videogamestore.repository.GameRepository;
import com.milan.videogamestore.repository.GameReviewRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final GameRepository gameRepository;
    private final AppUserRepository appUserRepository;
    private final GameReviewRepository reviewRepository;

    @PostMapping("/games/{gameId}/reviews")
    public String insertReviewForGame(@PathVariable Long gameId,
                                    @Valid @ModelAttribute("reviewForm") ReviewForm form,
                                    BindingResult bindingResult,
                                    Authentication authentication) {

        if (authentication == null) return "redirect:/login";

        if (bindingResult.hasErrors()) {
            return "redirect:games/" + gameId + "?reviewError=1";
        }

        Game game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));
        AppUser user = appUserRepository.findByUsernameWithRole(authentication.getName()).orElseThrow(() -> new IllegalStateException("Logged in user not found: " + authentication.getName()));

        Review review = reviewRepository.findByGame_IdAndUser_Username(gameId, authentication.getName()).orElseGet(Review::new);

        review.setGame(game);
        review.setUser(user);
        review.setComment(form.getComment());
        review.setRating(form.getRating());

        reviewRepository.save(review);

        return "redirect:/games/" + gameId + "#reviews";
    }

    @PostMapping("/games/{gameId}/reviews/delete")
    public String deleteMyReview(@PathVariable Long gameId, Authentication auth) {
        if (auth == null) return "redirect:/login";

        reviewRepository.findByGame_IdAndUser_Username(gameId, auth.getName())
                .ifPresent(reviewRepository::delete);
        return "redirect:/games/" + gameId + "#reviews";
    }
}
