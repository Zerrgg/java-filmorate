package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewImpl implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Review addReview(Review review) {
        return null;
    }

    @Override
    public Review updateReview(Review review) {
        return null;
    }

    @Override
    public void deleteReview(int reviewId) {

    }

    @Override
    public Review getReview(int reviewId) {
        return null;
    }

    @Override
    public List<Review> getFilmReviews(int filmId, int count) {
        return null;
    }
}
