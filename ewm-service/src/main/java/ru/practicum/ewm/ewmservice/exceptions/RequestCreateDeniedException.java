package ru.practicum.ewm.ewmservice.exceptions;

public class RequestCreateDeniedException extends RuntimeException {
    public RequestCreateDeniedException(String message) {
        super(message);
    }
}
