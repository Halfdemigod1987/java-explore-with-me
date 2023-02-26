package ru.practicum.ewm.ewmservice.services;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.ewmservice.dto.CommentDto;
import ru.practicum.ewm.ewmservice.dto.CommentNewDto;
import ru.practicum.ewm.ewmservice.dto.CommentUpdateDto;

import java.util.List;

@Service
public interface CommentService {

    List<CommentDto> findAllComments(Long eventId, Integer from, Integer size);

    List<CommentDto> findAllComments(Long userId, Long eventId, Integer from, Integer size);

    CommentDto findComment(Long commentId);

    CommentDto findComment(Long userId, Long commentId);

    CommentDto createComment(CommentNewDto commentNewDto, Long userId);

    CommentDto updateComment(CommentUpdateDto commentUpdateDto, Long userId, Long commentId);

    CommentDto updateComment(CommentUpdateDto commentUpdateDto, Long commentId);

    void deleteComment(Long userId, Long commentId);

    void deleteComment(Long commentId);
}
