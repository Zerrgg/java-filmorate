package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FeedMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .timestamp(rs.getTimestamp("time_stamp"))
                .userId(rs.getLong("user_id"))
                .eventType(rs.getString("event_type"))
                .eventId(rs.getLong("event_id"))
                .entityId(rs.getLong("entity_id"))
                .operation(rs.getString("operation"))
                .build();
        }
    }