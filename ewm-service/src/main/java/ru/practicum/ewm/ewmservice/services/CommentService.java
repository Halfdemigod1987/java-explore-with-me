package ru.practicum.ewm.ewmservice.services;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.ewmservice.dto.CommentDto;
import ru.practicum.ewm.ewmservice.dto.CommentNewDto;

import java.util.List;

@Service
public interface CommentService {

    List<CommentDto> findAllPublicEventComments(Long eventId, Integer from, Integer size);

    CommentDto findPublicEventCommentById(Long eventId, Long commentId);

    CommentDto createEventComment(CommentNewDto commentNewDto, Long userId, Long eventId);

    CommentDto updateEventComment(CommentNewDto commentNewDto, Long userId, Long eventId, Long commentId);

    void deleteComment(Long eventId, Long commentId);
}
