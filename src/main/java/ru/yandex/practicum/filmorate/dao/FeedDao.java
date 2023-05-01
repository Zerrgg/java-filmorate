package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.model.Operations;

import java.util.List;

public interface FeedDao {
    void add(long userId, long entityId, EventTypes eventType, Operations operation);

    List<Event> getFeed(long userId);
}