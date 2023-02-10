package ru.practicum.ewm.ewmservice.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder
@Jacksonized
public class CompilationNewDto {
    @NotNull
    List<Long> events;
    Boolean pinned;
    @NotBlank
    String title;
}
