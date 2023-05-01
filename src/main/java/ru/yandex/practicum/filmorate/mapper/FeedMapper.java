package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.model.Operations;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FeedMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .timestamp(rs.getTimestamp("time_stamp").getTime())
                .userId(rs.getLong("user_id"))
                .eventType(setEventType(rs.getString("event_type")))
                .eventId(rs.getLong("event_id"))
                .entityId(rs.getLong("entity_id"))
                .operation(setOperation(rs.getString("operation")))
                .build();
        }

    private Operations setOperation(String operation) {
        switch (operation) {
            case "ADD":
                return Operations.ADD;
            case "UPDATE":
                return Operations.UPDATE;
            case "REMOVE":
                return Operations.REMOVE;
            default:
                throw new ValidationException("Такой операции пока нет.");
        }
    }

    private EventTypes setEventType(String eventType) {
        switch (eventType) {
            case "LIKE":
                return EventTypes.LIKE;
            case "REVIEW":
                return EventTypes.REVIEW;
            case "FRIEND":
                return EventTypes.FRIEND;
            default:
                throw new ValidationException("Такого события пока нет.");
        }
    }
    }