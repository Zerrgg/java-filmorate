package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDao reviewDao;
    private final UserDao userDao;
    private final FilmDao filmDao;

    public Review add(Review review) {
        validator(review);
        return reviewDao.addReview(review);
    }

    public Review update(Review review) {
        validator(review);
        reviewDao.getReview(review.getReviewId());
        return reviewDao.updateReview(review);
    }

    public Review get(long reviewId) {
        return reviewDao.getReview(reviewId);
    }

    public void delete(long reviewId) {
        reviewDao.getReview(reviewId);
        reviewDao.deleteReview(reviewId);
    }

    public List<Review> getByFilmReviews(Long filmId, int count) {
        return reviewDao.getByFilmReviews(filmId, count);
    }

    public List<Review> getAll() {
        return reviewDao.getAll();
    }

    public void addLike(long id, long userId) {
        validatorLike(id, userId);
        reviewDao.addLike(id, userId);
    }

    public void addDislike(long id, long userId) {
        validatorLike(id, userId);
        reviewDao.addDislike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        validatorLike(id, userId);
        reviewDao.deleteLike(id, userId);
    }

    public void deleteDislike(long id, long userId) {
        validatorLike(id, userId);
        reviewDao.deleteDislike(id, userId);
    }

    private void validator(Review review) {
        Long filmId = review.getFilmId();
        Long userId = review.getUserId();
        if (userDao.get(userId) == null || filmDao.get(filmId) == null) {
            log.info("Передан некорректный id фильма и пользователя");
            throw new ValidationException("Не передан id");
        }
    }

    private void validatorLike(long id, long userId) {
        reviewDao.getReview(id);
        userDao.get(userId);
    }
}
