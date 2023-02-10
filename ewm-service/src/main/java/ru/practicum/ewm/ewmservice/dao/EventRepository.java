package ru.practicum.ewm.ewmservice.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.ewmservice.model.Event;
import ru.practicum.ewm.ewmservice.model.User;

import java.util.List;

@Repository
public interface EventRepository extends
        JpaRepository<Event, Long>,
        EventRepositoryCustom,
        JpaSpecificationExecutor<Event> {

    List<Event> findAllByInitiator(User initiator, Pageable pageable);

    List<Event> findAllByCategoryId(Long categoryId);

}
