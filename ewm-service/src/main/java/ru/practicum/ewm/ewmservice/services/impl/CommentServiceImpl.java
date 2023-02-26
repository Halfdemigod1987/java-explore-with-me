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
import ru.practicum.ewm.ewmservice.dto.CommentUpdateDto;
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

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(
                        String.format("Comment with id=%d was not found", commentId)));
    }

    @Override
    public List<CommentDto> findAllComments(Long eventId, Integer from, Integer size) {
        Pageable pageable;
        if (size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "created"));
        } else {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "created"));
        }

        if (eventId != null) {
            Event event = findEventById(eventId);
            return commentRepository.findAllByEvent(event, pageable)
                    .stream()
                    .map(commentsMapper::commentToCommentDto)
                    .collect(Collectors.toList());
        } else {
            return commentRepository.findAll(pageable)
                    .stream()
                    .map(commentsMapper::commentToCommentDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<CommentDto> findAllComments(Long userId, Long eventId, Integer from, Integer size) {
        User user = findUserById(userId);

        Pageable pageable;
        if (size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "created"));
        } else {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "created"));
        }

        if (eventId != null) {
            Event event = findEventById(eventId);
            return commentRepository.findAllByEventAndAuthor(event, user, pageable)
                    .stream()
                    .map(commentsMapper::commentToCommentDto)
                    .collect(Collectors.toList());
        } else {
            return commentRepository.findAllByAuthor(user, pageable)
                    .stream()
                    .map(commentsMapper::commentToCommentDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public CommentDto findComment(Long commentId) {
        return commentsMapper.commentToCommentDto(findCommentById(commentId));
    }

    @Override
    public CommentDto findComment(Long userId, Long commentId) {
        User user = findUserById(userId);
        Comment comment = findCommentById(commentId);
        if (!comment.getAuthor().equals(user)) {
            throw new CommentNotFoundException(String.format("Comment with id=%d was not found", commentId));
        }
        return commentsMapper.commentToCommentDto(comment);
    }

    @Override
    public CommentDto createComment(CommentNewDto commentNewDto, Long userId) {
        User user = findUserById(userId);
        Event event = findEventById(commentNewDto.getEvent());

        Comment comment = commentsMapper.commentNewDtoToComment(commentNewDto);
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());

        return commentsMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateComment(CommentUpdateDto commentUpdateDto, Long userId, Long commentId) {
        User user = findUserById(userId);
        Comment comment = findCommentById(commentId);

        if (!comment.getAuthor().equals(user)) {
            throw  new CommentNotFoundException(
                    String.format("Comment with id=%d was not found", commentId));
        }

        comment.setText(commentUpdateDto.getText());

        return commentsMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateComment(CommentUpdateDto commentUpdateDto, Long commentId) {
        Comment comment = findCommentById(commentId);
        comment.setText(commentUpdateDto.getText());
        return commentsMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        User user = findUserById(userId);
        Comment comment = findCommentById(commentId);
        if (!comment.getAuthor().equals(user)) {
            throw new CommentNotFoundException(String.format("Comment with id=%d was not found", commentId));
        }
        commentRepository.delete(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = findCommentById(commentId);
        commentRepository.delete(comment);
    }
}
