package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;
import java.util.List;

public interface ReviewDao {
    Review addReview(Review review);

    Review updateReview(Review review);

    void deleteReview(int reviewId);

    Review getReview(int reviewId);

    List<Review> getFilmReviews(int filmId, int count);
}
