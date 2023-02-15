package ru.practicum.ewm.ewmservice.exceptions;

public class EventCreateDeniedException extends RuntimeException {
    public EventCreateDeniedException(String message) {
        super(message);
    }
}
