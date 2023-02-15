package ru.practicum.ewm.ewmservice.exceptions;

public class RequestStatusChangeDeniedException extends RuntimeException {
    public RequestStatusChangeDeniedException(String message) {
        super(message);
    }
}
