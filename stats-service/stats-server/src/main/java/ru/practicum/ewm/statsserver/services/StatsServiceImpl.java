package ru.practicum.ewm.statsserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.statsserver.dao.StatsRepository;
import ru.practicum.ewm.statsdto.*;
import ru.practicum.ewm.statsserver.dto.EndpointHitMapper;
import ru.practicum.ewm.statsserver.dto.StatsMapper;
import ru.practicum.ewm.statsserver.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final EndpointHitMapper endpointHitMapper = EndpointHitMapper.INSTANCE;
    private final StatsMapper statsMapper = StatsMapper.INSTANCE;


    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique && uris != null) {
            return statsRepository
                    .findUniqueEndpointHitsByUriInAndTimeStampAfterAndTimeStampBefore(start, end, uris)
                    .stream()
                    .map(statsMapper::statsToStatsDto)
                    .collect(Collectors.toList());
        } else if (unique) {
            return statsRepository
                    .findUniqueEndpointHitsByTimeStampAfterAndTimeStampBefore(start, end)
                    .stream()
                    .map(statsMapper::statsToStatsDto)
                    .collect(Collectors.toList());
        } else if (uris != null) {
            return statsRepository
                    .findEndpointHitsByUriInAndTimeStampAfterAndTimeStampBefore(start, end, uris)
                    .stream()
                    .map(statsMapper::statsToStatsDto)
                    .collect(Collectors.toList());
        } else {
            return statsRepository
                    .findEndpointHitsByTimeStampAfterAndTimeStampBefore(start, end)
                    .stream()
                    .map(statsMapper::statsToStatsDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public EndpointHitDto createHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = endpointHitMapper.endpointHitDtoToEndpointHit(endpointHitDto);
        return endpointHitMapper.endpointHitToEndpointHitDto(statsRepository.save(endpointHit));
    }
}
