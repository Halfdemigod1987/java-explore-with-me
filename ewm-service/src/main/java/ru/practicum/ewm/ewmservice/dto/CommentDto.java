package ru.practicum.ewm.ewmservice.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class CommentDto {
    Long id;
    String text;
    Long author;
    Long event;
    LocalDateTime created;
}
