package ru.practicum.ewm.ewmservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.ewmservice.dao.CommentRepository;
import ru.practicum.ewm.ewmservice.dao.EventRepository;
import ru.practicum.ewm.ewmservice.dao.UserRepository;
import ru.practicum.ewm.ewmservice.dto.CommentDto;
import ru.practicum.ewm.ewmservice.dto.CommentNewDto;
import ru.practicum.ewm.ewmservice.dto.mappers.CommentsMapper;
import ru.practicum.ewm.ewmservice.exceptions.CommentNotFoundException;
import ru.practicum.ewm.ewmservice.exceptions.EventNotFoundException;
import ru.practicum.ewm.ewmservice.exceptions.UserNotFoundException;
import ru.practicum.ewm.ewmservice.model.Comment;
import ru.practicum.ewm.ewmservice.model.Event;
import ru.practicum.ewm.ewmservice.model.EventState;
import ru.practicum.ewm.ewmservice.model.User;
import ru.practicum.ewm.ewmservice.services.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentsMapper commentsMapper;

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id=%d was not found", id)));
    }

    private Event findEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(
                        String.format("Event with id=%d was not found", id)));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new EventNotFoundException(String.format("Event with id=%d was not found", id));
        }
        return event;
    }

    private Comment findCommentById(Long eventId, Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(
                        String.format("Comment with id=%d to event with id-%d was not found", commentId, eventId)));
    }

    @Override
    public List<CommentDto> findAllPublicEventComments(Long eventId, Integer from, Integer size) {
        Event event = findEventById(eventId);

        Pageable pageable;
        if (size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "created"));
        } else {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "created"));
        }

        return commentRepository.findAllByEvent(event, pageable)
                .stream()
                .map(commentsMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto findPublicEventCommentById(Long eventId, Long commentId) {
        findEventById(eventId);

        return commentsMapper.commentToCommentDto(findCommentById(eventId, commentId));
    }

    @Override
    public CommentDto createEventComment(CommentNewDto commentNewDto, Long userId, Long eventId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);

        Comment comment = commentsMapper.commentNewDtoToComment(commentNewDto);
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());

        return commentsMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateEventComment(CommentNewDto commentNewDto, Long userId, Long eventId, Long commentId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);
        Comment comment = findCommentById(eventId, commentId);

        if (!comment.getAuthor().equals(user) || !comment.getEvent().equals(event)) {
            throw  new CommentNotFoundException(
                    String.format("Comment with id=%d to event with id-%d was not found", commentId, eventId));
        }

        comment.setText(commentNewDto.getText());

        return commentsMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long eventId, Long commentId) {
        Event event = findEventById(eventId);
        Comment comment = findCommentById(eventId, commentId);

        if (!comment.getEvent().equals(event)) {
            throw  new CommentNotFoundException(
                    String.format("Comment with id=%d to event with id-%d was not found", commentId, eventId));
        }

        commentRepository.delete(comment);
    }
}
