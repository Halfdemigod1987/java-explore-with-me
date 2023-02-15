package ru.practicum.ewm.ewmservice.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

@Value
@Builder
@Jacksonized
public class CategoryNewDto {
    @NotBlank
    String name;
}
