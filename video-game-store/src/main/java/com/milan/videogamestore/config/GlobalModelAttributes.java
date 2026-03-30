package com.milan.videogamestore.config;

import com.milan.videogamestore.model.cart.Cart;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("cartItemCount")
    public int cartItemCount(HttpSession session) {
        Object obj = session.getAttribute("cart");
        if (!(obj instanceof Cart cart)) return 0;

        // broj stavki, tj. razlicite igre:
        // return cart.getQuantities().size();

        // quantity, tj. zbroj
        return cart.getQuantities().values().stream().mapToInt(Integer::intValue).sum();
    }
}
