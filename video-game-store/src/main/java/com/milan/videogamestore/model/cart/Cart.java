package com.milan.videogamestore.model.cart;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart implements Serializable {
    private final Map<Long, Integer> quantities = new LinkedHashMap<>();

    public Map<Long, Integer> getQuantities() {
        return quantities;
    }

    public void add(Long gameId, int qty) {
        if (qty <= 0) return;
        quantities.merge(gameId, qty, Integer::sum);
    }

    public void setQuantity(Long gameId, int qty) {
        if (qty <= 0) quantities.remove(gameId);
        else quantities.put(gameId, qty);
    }

    public void remove(Long gameId) {
        quantities.remove(gameId);
    }

    public void clear() {
        quantities.clear();
    }
}
