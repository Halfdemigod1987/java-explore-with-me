package ru.practicum.ewm.statsdto;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@AllArgsConstructor
@Jacksonized
public class StatsDto {
    String app;
    String uri;
    Long hits;
}
