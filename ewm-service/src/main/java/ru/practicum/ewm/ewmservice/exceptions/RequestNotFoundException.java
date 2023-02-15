package ru.practicum.ewm.ewmservice.exceptions;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(String message) {
        super(message);
    }
}
