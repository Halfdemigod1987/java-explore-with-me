package ru.practicum.ewm.statsserver.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.statsserver.model.EndpointHit;
import ru.practicum.ewm.statsdto.*;

@Mapper(uses = {EndpointHit.class})
public interface EndpointHitMapper {
    EndpointHitMapper INSTANCE = Mappers.getMapper(EndpointHitMapper.class);

    EndpointHitDto endpointHitToEndpointHitDto(EndpointHit endpointHit);

    EndpointHit endpointHitDtoToEndpointHit(EndpointHitDto endpointHitDto);
}
