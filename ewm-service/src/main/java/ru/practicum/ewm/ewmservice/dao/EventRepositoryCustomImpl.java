package ru.practicum.ewm.ewmservice.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.ewmservice.model.Event;
import ru.practicum.ewm.ewmservice.model.EventState;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventRepositoryCustomImpl implements EventRepositoryCustom {
    private final EventRepository eventRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public EventRepositoryCustomImpl(@Lazy EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> findAllEventsWithFilter(List<Long> users,
                                               List<EventState> states,
                                               List<Long> categories,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Pageable pageable) {
        return eventRepository.findAll((Specification<Event>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (users != null) {
                CriteriaBuilder.In<Long> inInitiator = builder.in(root.get("initiator"));
                users.forEach(inInitiator::value);
                predicates.add(inInitiator);
            }
            if (states != null) {
                CriteriaBuilder.In<EventState> inState = builder.in(root.get("state"));
                states.forEach(inState::value);
                predicates.add(inState);
            }
            if (categories != null) {
                CriteriaBuilder.In<Long> inCategory = builder.in(root.get("category"));
                categories.forEach(inCategory::value);
                predicates.add(inCategory);
            }
            if (rangeStart != null) {
                Predicate eventDateGreater = builder.greaterThan(root.get("eventDate"), rangeStart);
                predicates.add(eventDateGreater);
            }
            if (rangeEnd != null) {
                Predicate eventDateLower = builder.lessThan(root.get("eventDate"), rangeEnd);
                predicates.add(eventDateLower);
            }
            return builder.and(predicates.toArray(new Predicate[]{}));
        }, pageable).getContent();
    }

    @Override
    public List<Event> findAllPublicEventsWithFilter(String text,
                                                     List<Long> categories,
                                                     Boolean paid,
                                                     LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd,
                                                     Boolean onlyAvailable,
                                                     String sort,
                                                     Integer from,
                                                     Integer size) {

        StringBuilder sb = new StringBuilder();
        sb.append("select e from Event e ");
        if (onlyAvailable != null && onlyAvailable) {
            sb.append("left join Request r on e.id = request.event_id ");
        }
        sb.append("where e.state = :state ");
        if (text != null) {
            sb.append("and (lower(e.annotation) like :text or lower(e.description) like :text) ");
        }
        if (categories != null) {
            sb.append("and e.category.id in (:categories) ");
        }
        if (paid != null) {
            sb.append("and e.paid = :paid ");
        }
        if (rangeStart != null) {
            sb.append("and e.eventDate >= :rangeStart ");
        }
        if (rangeEnd != null) {
            sb.append("and e.eventDate <= :rangeEnd ");
        }
        if (rangeStart == null || rangeEnd == null) {
            sb.append("and e.eventDate >= :currentDate ");
        }
        if (onlyAvailable != null && onlyAvailable) {
            sb.append("group by e having count(r) < e.participantLimit or e.participantLimit = 0 ");
        }
        if (sort != null && sort.equals("VIEWS")) {
            sb.append("order by e.views desc");
        } else {
            sb.append("order by e.eventDate asc");
        }

        TypedQuery<Event> query = entityManager.createQuery(sb.toString(), Event.class);

        query.setParameter("state", EventState.PUBLISHED);
        if (text != null) {
            query.setParameter("text", "%" + text.toLowerCase() + "%");
        }
        if (categories != null) {
            query.setParameter("categories", categories);
        }
        if (paid != null) {
            query.setParameter("paid", paid);
        }
        if (rangeStart != null) {
            query.setParameter("rangeStart", rangeStart);
        }
        if (rangeEnd != null) {
            query.setParameter("rangeEnd", rangeEnd);
        }
        if (rangeStart == null || rangeEnd == null) {
            query.setParameter("currentDate", LocalDateTime.now());
        }

        if (size != null) {
            query.setFirstResult(from);
            query.setMaxResults(size);
        }
        return query.getResultList();

    }
}
