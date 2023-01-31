package ru.practicum.ewm.statsserver.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Stats {
    String app;
    String uri;
    Long hits;
}
