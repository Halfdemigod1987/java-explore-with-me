package ru.practicum.ewm.ewmservice.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.ewmservice.model.Comment;
import ru.practicum.ewm.ewmservice.model.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEvent(Event event, Pageable pageable);

    Optional<Comment> findByEventAndId(Event event, Long id);
}
