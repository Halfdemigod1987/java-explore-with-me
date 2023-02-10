package ru.practicum.ewm.ewmservice.services;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.ewmservice.dto.*;
import ru.practicum.ewm.ewmservice.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface EventService {

    List<EventShortDto> findAllUserEvents(Long userId, Integer from, Integer size);

    EventFullDto createEvent(EventNewDto eventNewDto, Long userId);

    EventFullDto findUserEventById(Long userId, Long eventId);

    EventFullDto updateEvent(EventUpdatetDto eventUpdatetDto, Long userId, Long eventId);

    List<RequestDto> findAllEventRequests(Long userId, Long eventId);

    RequestUpdateResultDto updateEventRequests(RequestUpdateDto requestUpdateDto, Long userId, Long eventId);

    List<EventFullDto> findAllEvents(List<Long> users,
                                     List<EventState> states,
                                     List<Long> categories,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Integer from,
                                     Integer size);

    List<EventFullDto> findAllPublicEvents(String text,
                                           List<Long> categories,
                                           Boolean paid,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           Boolean onlyAvailable,
                                           String sort,
                                           Integer from,
                                           Integer size);

    EventFullDto findPublicEventById(Long id);
}
