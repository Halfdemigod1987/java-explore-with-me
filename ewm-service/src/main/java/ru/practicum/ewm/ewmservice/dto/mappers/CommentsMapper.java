package ru.practicum.ewm.ewmservice.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.ewmservice.dto.CommentDto;
import ru.practicum.ewm.ewmservice.dto.CommentNewDto;
import ru.practicum.ewm.ewmservice.model.Comment;

@Mapper(componentModel = "spring")
@Component
public interface CommentsMapper {

    Comment commentNewDtoToComment(CommentNewDto commentNewDto);

    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "author.id", target = "author")
    CommentDto commentToCommentDto(Comment comment);
}
