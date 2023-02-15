package ru.practicum.ewm.statsserver.services;

import ru.practicum.ewm.statsdto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    EndpointHitDto createHit(EndpointHitDto endpointHitDto);
}
