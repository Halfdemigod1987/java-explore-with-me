package ru.practicum.ewm.ewmservice.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.ewmservice.dto.EventFullDto;
import ru.practicum.ewm.ewmservice.dto.EventNewDto;
import ru.practicum.ewm.ewmservice.dto.EventShortDto;
import ru.practicum.ewm.ewmservice.model.Event;

@Component
@Mapper(uses = {CategoryMapper.class, UserMapper.class}, componentModel = "spring")
public interface EventMapper {

    EventShortDto eventToEventShortDto(Event event);

    EventFullDto eventToEventFullDto(Event event);

    @Mapping(target = "category", ignore = true)
    Event eventNewDtoToEvent(EventNewDto eventNewDto);
}
