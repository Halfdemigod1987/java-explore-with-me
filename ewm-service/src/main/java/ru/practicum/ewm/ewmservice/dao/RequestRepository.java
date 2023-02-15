package ru.practicum.ewm.ewmservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.ewmservice.model.Event;
import ru.practicum.ewm.ewmservice.model.Request;
import ru.practicum.ewm.ewmservice.model.RequestStatus;
import ru.practicum.ewm.ewmservice.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByEvent(Event event);

    List<Request> findAllByEventAndStatus(Event event, RequestStatus requestStatus);

    List<Request> findAllByIdIn(List<Long> id);

    Optional<Request> findAllByRequesterAndEvent(User user, Event event);

    List<Request> findAllByRequester(User user);

    List<Request> findAllByEventInitiatorAndStatus(User user, RequestStatus requestStatus);

    List<Request> findAllByEventInAndStatus(List<Event> events, RequestStatus requestStatus);

}
