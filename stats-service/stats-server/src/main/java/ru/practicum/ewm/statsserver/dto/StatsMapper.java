package ru.practicum.ewm.statsserver.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.statsdto.StatsDto;
import ru.practicum.ewm.statsserver.model.Stats;

@Mapper(uses = {Stats.class})
public interface StatsMapper {
    StatsMapper INSTANCE = Mappers.getMapper(StatsMapper.class);

    StatsDto statsToStatsDto(Stats stats);
}
