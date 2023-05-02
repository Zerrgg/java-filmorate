package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReviewMapper implements RowMapper<Review> {
    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long reviewId = rs.getLong("reviews_id");
        Long userId = rs.getLong("user_id");
        Long filmId = rs.getLong("film_id");
        String content = rs.getString("content");
        Boolean isPositive = rs.getBoolean("isPositive");
        Long useful = rs.getLong("useful");

        return rs.wasNull() ? null : new Review(reviewId, content, isPositive, userId, filmId, useful);
    }

    public Map<String, Object> toMap(Review review) {
        Map<String, Object> values = new HashMap<>();
        values.put("user_id", review.getUserId());
        values.put("film_id", review.getFilmId());
        values.put("content", review.getContent());
        values.put("isPositive", review.getIsPositive());
        values.put("useful", review.getUseful());
        return values;
    }
}
