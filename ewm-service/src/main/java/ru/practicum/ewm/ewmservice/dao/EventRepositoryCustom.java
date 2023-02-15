package ru.practicum.ewm.ewmservice.dao;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.ewmservice.model.Event;
import ru.practicum.ewm.ewmservice.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositoryCustom {

    List<Event> findAllEventsWithFilter(List<Long> users,
                                        List<EventState> states,
                                        List<Long> categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Pageable pageable);

    List<Event> findAllPublicEventsWithFilter(String text,
                                              List<Long> categories,
                                              Boolean paid,
                                              LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd,
                                              Boolean onlyAvailable,
                                              String sort,
                                              Integer from,
                                              Integer size);
}
