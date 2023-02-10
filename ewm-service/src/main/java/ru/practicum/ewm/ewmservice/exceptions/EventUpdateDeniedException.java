package ru.practicum.ewm.ewmservice.exceptions;

public class EventUpdateDeniedException extends RuntimeException {
    public EventUpdateDeniedException(String message) {
        super(message);
    }
}
