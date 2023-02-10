package ru.practicum.ewm.ewmservice.exceptions;

public class RequestUpdateDeniedException extends RuntimeException {
    public RequestUpdateDeniedException(String message) {
        super(message);
    }
}
