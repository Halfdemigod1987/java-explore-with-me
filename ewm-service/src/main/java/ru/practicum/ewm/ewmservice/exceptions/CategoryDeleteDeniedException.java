package ru.practicum.ewm.ewmservice.exceptions;

public class CategoryDeleteDeniedException extends RuntimeException {
    public CategoryDeleteDeniedException(String message) {
        super(message);
    }
}
