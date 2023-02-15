package ru.practicum.ewm.ewmservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.ewmservice.Utils;
import ru.practicum.ewm.ewmservice.dao.*;
import ru.practicum.ewm.ewmservice.dto.*;
import ru.practicum.ewm.ewmservice.dto.mappers.EventMapper;
import ru.practicum.ewm.ewmservice.dto.mappers.RequestMapper;
import ru.practicum.ewm.ewmservice.exceptions.*;
import ru.practicum.ewm.ewmservice.model.*;
import ru.practicum.ewm.ewmservice.services.EventService;
import ru.practicum.ewm.statsclient.StatsClient;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final StatsClient statsClient;

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id=%d was not found", id)));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        String.format("Category with id=%d was not found", id)));
    }

    private Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(
                        String.format("Event with id=%d was not found", id)));
    }

    private Event findEventByIdAndUser(Long userId, Long eventId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);

        if (!event.getInitiator().equals(user)) {
            throw  new EventNotFoundException(String.format("Event with id=%d was not found", eventId));
        }
        return event;
    }

    @Override
    public List<EventShortDto> findAllUserEvents(Long userId, Integer from, Integer size) {
        User user = findUserById(userId);

        Pageable pageable;
        if (size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageable = PageRequest.of(from / size, size);
        }

        List<Request> requests = requestRepository.findAllByEventInitiatorAndStatus(user, RequestStatus.CONFIRMED);

        return eventRepository.findAllByInitiator(user, pageable)
                .stream()
                .map(eventMapper::eventToEventShortDto)
                .peek(eventShortDto -> eventShortDto.setConfirmedRequests(
                        requests.stream().filter(request -> request.getEvent().getId().equals(eventShortDto.getId())).count()))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(EventNewDto eventNewDto, Long userId) {
        User user = findUserById(userId);

        Event event = eventMapper.eventNewDtoToEvent(eventNewDto);
        event.setInitiator(user);
        Category category = findCategoryById(eventNewDto.getCategory());
        event.setCategory(category);
        event.setState(EventState.PENDING);

        return eventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto findUserEventById(Long userId, Long eventId) {
        Event event = findEventByIdAndUser(userId, eventId);

        List<Request> requests = requestRepository.findAllByEventAndStatus(event, RequestStatus.CONFIRMED);
        EventFullDto eventFullDto = eventMapper.eventToEventFullDto(event);
        eventFullDto.setConfirmedRequests((long) requests.size());

        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(EventUpdatetDto eventUpdatetDto, Long userId, Long eventId) {
        Event event = findEventById(eventId);

        if (userId != null) {
            User user = findUserById(userId);
            if (!event.getInitiator().equals(user)) {
                throw new EventNotFoundException(String.format("Event with id=%d was not found", eventId));
            }
        }

        if (userId != null && event.getState().equals(EventState.PUBLISHED)) {
            throw new EventUpdateDeniedException("Only pending or canceled events can be changed");
        }

        if (eventUpdatetDto.getStateAction() != null) {
            if (eventUpdatetDto.getStateAction().equals(EventAction.PUBLISH_EVENT)
                    && !event.getState().equals(EventState.PENDING)) {
                throw new EventUpdateDeniedException(
                        String.format("Cannot publish the event because it's not in the right state: %s", event.getState()));
            } else if (eventUpdatetDto.getStateAction().equals(EventAction.REJECT_EVENT)
                    && !event.getState().equals(EventState.PENDING)) {
                throw new EventUpdateDeniedException(
                        String.format("Cannot reject the event because it's not in the right state: %s", event.getState()));
            }
        }

        copyProperties(eventUpdatetDto, event, Utils.getNullPropertyNames(eventUpdatetDto));

        if (eventUpdatetDto.getStateAction() != null) {
            switch (eventUpdatetDto.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
            }
        }

        List<Request> requests = requestRepository.findAllByEventAndStatus(event, RequestStatus.CONFIRMED);
        EventFullDto eventFullDto = eventMapper.eventToEventFullDto(eventRepository.save(event));
        eventFullDto.setConfirmedRequests((long) requests.size());

        return eventFullDto;
    }

    @Override
    public List<RequestDto> findAllEventRequests(Long userId, Long eventId) {
        Event event = findEventByIdAndUser(userId, eventId);

        return requestRepository.findAllByEvent(event)
                .stream()
                .map(requestMapper::requestToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestUpdateResultDto updateEventRequests(RequestUpdateDto requestUpdateDto, Long userId, Long eventId) {
        Event event = findEventByIdAndUser(userId, eventId);

        List<Request> requests = requestRepository.findAllByEventAndStatus(event, RequestStatus.CONFIRMED);
        if (requestUpdateDto.getStatus().equals(RequestStatus.CONFIRMED)
                && event.getParticipantLimit() != 0
                && requests.size() + requestUpdateDto.getRequestIds().size() > event.getParticipantLimit()) {
            throw new RequestStatusChangeDeniedException("The participant limit has been reached");
        }

        List<Request> updatedRequests = requestRepository.findAllByIdIn(requestUpdateDto.getRequestIds());
        updatedRequests.forEach(request -> {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new RequestUpdateBadRequestException("Request must have status PENDING");
            }
            request.setStatus(requestUpdateDto.getStatus());
        });
        requests = requestRepository.saveAll(updatedRequests);

        return RequestUpdateResultDto
                .builder()
                .confirmedRequests(requests
                        .stream()
                        .filter(request -> request.getStatus().equals(RequestStatus.CONFIRMED))
                        .map(requestMapper::requestToRequestDto)
                        .collect(Collectors.toList()))
                .rejectedRequests(requests
                        .stream()
                        .filter(request -> request.getStatus().equals(RequestStatus.REJECTED))
                        .map(requestMapper::requestToRequestDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<EventFullDto> findAllEvents(List<Long> users,
                                            List<EventState> states,
                                            List<Long> categories,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            Integer from,
                                            Integer size) {
        Pageable pageable;
        if (size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageable = PageRequest.of(from / size, size);
        }

        List<Event> events = eventRepository.findAllEventsWithFilter(users, states, categories, rangeStart, rangeEnd, pageable);
        List<Request> requests = requestRepository.findAllByEventInAndStatus(events, RequestStatus.CONFIRMED);

        return events
                .stream()
                .map(eventMapper::eventToEventFullDto)
                .peek(eventFullDto -> eventFullDto.setConfirmedRequests(
                        requests.stream().filter(request -> request.getEvent().getId().equals(eventFullDto.getId())).count()))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> findAllPublicEvents(String text,
                                                  List<Long> categories,
                                                  Boolean paid,
                                                  LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd,
                                                  Boolean onlyAvailable,
                                                  String sort,
                                                  Integer from,
                                                  Integer size,
                                                  String uri,
                                                  String ip) {

        statsClient.createHit("java-explore-with-me", uri, ip, LocalDateTime.now());

        List<Event> events = eventRepository.findAllPublicEventsWithFilter(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size);
        List<Request> requests = requestRepository.findAllByEventInAndStatus(events, RequestStatus.CONFIRMED);

        return events
                .stream()
                .map(eventMapper::eventToEventFullDto)
                .peek(eventFullDto -> eventFullDto.setConfirmedRequests(
                        requests.stream().filter(request -> request.getEvent().getId().equals(eventFullDto.getId())).count()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto findPublicEventById(Long id, String uri, String ip) {
        statsClient.createHit("java-explore-with-me", uri, ip, LocalDateTime.now());

        Event event = findEventById(id);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new EventNotFoundException(String.format("Event with id=%d was not found", id));
        }
        event.setViews(event.getViews() == null ? 1 : event.getViews() + 1);
        return eventMapper.eventToEventFullDto(eventRepository.save(event));
    }
}
