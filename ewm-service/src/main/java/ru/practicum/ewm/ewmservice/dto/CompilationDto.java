package ru.practicum.ewm.ewmservice.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class CompilationDto {
    List<EventShortDto> events;
    Integer id;
    Boolean pinned;
    String title;
}
