package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;
import java.util.List;

public interface ReviewDao {
    Review addReview(Review review);

    Review updateReview(Review review);

    void deleteReview(long reviewId);

    Review getReview(long reviewId);

    List<Review> getByFilmReviews(long filmId, int count);

    List<Review> getAll();

    void addLike(long id, long userId);

    void addDislike(long id, long userId);

    void deleteLike(long id, long userId);

    void deleteDislike(long id, long userId);
}
