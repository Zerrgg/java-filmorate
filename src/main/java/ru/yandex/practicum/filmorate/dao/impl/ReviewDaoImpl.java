package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewDaoImpl implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;
    private final ReviewMapper reviewMapper;

    @Override
    public Review addReview(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("reviews_id");
        review.setReviewId(simpleJdbcInsert.executeAndReturnKey(reviewMapper.toMap(review)).longValue());
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        String sql = "UPDATE reviews SET content = ?, isPositive = ? WHERE reviews_id = ?";
        jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getReviewId());
        return getReview(review.getReviewId());
    }

    @Override
    public void deleteReview(long reviewId) {
        String sql = "DELETE FROM reviews WHERE reviews_id = ?";
        jdbcTemplate.update(sql, reviewId);
    }

    @Override
    public Review getReview(long reviewId) {
        try {
            String sql = "SELECT * FROM reviews WHERE reviews_id = ?";
            return jdbcTemplate.queryForObject(sql, reviewMapper, reviewId);
        } catch (RuntimeException e) {
            log.info("Отзыва с id - {} не существует в базе", reviewId);
            throw new ObjectNotFoundException("Отзыв не найден");
        }
    }

    @Override
    public List<Review> getByFilmReviews(long filmId, int count) {
        String sql = "SELECT * FROM reviews " +
                "WHERE film_id = ? " +
                "ORDER BY useful DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, reviewMapper, filmId, count);
    }

    @Override
    public List<Review> getAll() {
        String sql = "SELECT * FROM reviews ORDER BY useful DESC";
        return jdbcTemplate.query(sql, reviewMapper);
    }

    @Override
    public void addLike(long id, long userId) {
        Long useful = getUseful(id);
        useful++;
        Boolean isPositive = true;
        addLikesOrDislike(id, userId, useful, isPositive);
    }

    @Override
    public void addDislike(long id, long userId) {
        long useful = getUseful(id);
        useful--;
        Boolean isPositive = false;
        addLikesOrDislike(id, userId, useful, isPositive);
    }

    @Override
    public void deleteLike(long id, long userId) {
        Long useful = getUseful(id);
        useful--;
        deleteLikesOrDislike(id, userId, useful);
    }

    @Override
    public void deleteDislike(long id, long userId) {
        long useful = getUseful(id);
        useful++;
        deleteLikesOrDislike(id, userId, useful);
    }

    private Long getUseful(long reviewId) {
        String sql = "SELECT useful FROM reviews WHERE reviews_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, reviewId);
    }

    private void addLikesOrDislike(long id, long userId, long useful, Boolean isPositive) {
        String sqlAdd = "INSERT INTO review_likes(reviews_id, user_id, isPositive) VALUES (?,?,?)";
        jdbcTemplate.update(sqlAdd, id, userId, isPositive);
        addUseful(id, useful);
    }

    private void deleteLikesOrDislike(long id, long userId, long useful) {
        String sqlDelete = "DELETE FROM review_likes WHERE reviews_id = ? AND  user_id = ?";
        jdbcTemplate.update(sqlDelete, id, userId);
        addUseful(id, useful);
    }

    private void addUseful(long id, long useful) {
        String sqlUpdateUseful = "UPDATE reviews SET useful = ? WHERE reviews_id = ?";
        jdbcTemplate.update(sqlUpdateUseful, useful, id);
    }
}
