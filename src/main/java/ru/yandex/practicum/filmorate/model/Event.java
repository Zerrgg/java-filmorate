package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

@Data
@Builder
public class Event {
    @PastOrPresent
    private long timestamp;
    @NotNull
    private long userId;
    @NotNull
    private EventTypes eventType;
    @NotNull
    private Operations operation;
    private long eventId;
    @NotNull
    private long entityId;
}