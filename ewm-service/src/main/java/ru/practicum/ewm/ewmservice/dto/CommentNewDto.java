package ru.practicum.ewm.ewmservice.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder
@Jacksonized
public class CommentNewDto {
    @NotNull
    Long event;
    @Size(min = 1, max = 2000)
    @NotBlank
    String text;
}
