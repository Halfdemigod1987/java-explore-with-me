package ru.practicum.ewm.ewmservice.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.ewmservice.dto.RequestDto;
import ru.practicum.ewm.ewmservice.model.Request;

@Component
@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    RequestDto requestToRequestDto(Request request);
}
