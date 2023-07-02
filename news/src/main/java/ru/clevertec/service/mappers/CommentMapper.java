package ru.clevertec.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.repository.entity.Comment;
import ru.clevertec.service.dto.CommentDto;
import ru.clevertec.service.dto.CommentToPostDto;

@Mapper
public interface CommentMapper {

    @Mapping(target = "news", ignore = true)
    CommentDto toDto(Comment entity);

    Comment toEntityWithNews(CommentDto dto);

    Comment commentPostToEntity(CommentToPostDto commentToPostDto);
}
