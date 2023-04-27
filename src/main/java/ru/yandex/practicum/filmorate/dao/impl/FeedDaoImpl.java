package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FeedDao;
import ru.yandex.practicum.filmorate.mapper.FeedMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.model.Operations;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FeedDaoImpl implements FeedDao {
    private final JdbcTemplate jdbcTemplate;
    private final FeedMapper feedMapper;

    @Override
    public void add(long userId, long entityId, EventTypes eventType, Operations operation) {
        String sql = "INSERT INTO feed (user_id, event_type, operation, entity_id) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, userId, eventType.toString(), operation.toString(), entityId);
    }

    @Override
    public List<Event> getFeed(long userId) {
        String sql = "SELECT *\n" +
                "FROM feed\n" +
                "WHERE user_id = ?\n" +
                "ORDER BY time_stamp DESC,\n" +
                "event_id DESC";
        return jdbcTemplate.query(sql, feedMapper, userId);
    }
}