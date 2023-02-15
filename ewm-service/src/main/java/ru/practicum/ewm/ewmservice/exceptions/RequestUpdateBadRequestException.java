package ru.practicum.ewm.ewmservice.exceptions;

public class RequestUpdateBadRequestException extends RuntimeException {
    public RequestUpdateBadRequestException(String message) {
        super(message);
    }
}
