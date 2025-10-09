package com.example;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReviewRepository {

    private final AtomicLong idCounter = new AtomicLong(0);

    private final List<Review> reviews = new CopyOnWriteArrayList<>();

    @PostConstruct
    void initializeData() {
        save(new Review(null, 1L, "gamer123", 5, "Неймовірна гра! 10/10"));
        save(new Review(null, 1L, "another_player", 4, "Хороший сюжет, але є баги."));
        save(new Review(null, 3L, "soulslike_fan", 5, "Шедевр від FromSoftware!"));
    }

    public List<Review> findByGameId(Long gameId) {
        return reviews.stream()
                .filter(review -> review.gameId.equals(gameId))
                .collect(Collectors.toList());
    }

    public Review save(Review review) {
        if (review.id == null) {
            review.id = idCounter.incrementAndGet();
        }
        reviews.add(review);
        return review;
    }

    public List<Review> findAll() {
        return List.copyOf(reviews);
    }
}
