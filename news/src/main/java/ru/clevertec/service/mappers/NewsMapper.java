package ru.clevertec.service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.repository.entity.News;
import ru.clevertec.service.dto.NewsDto;
import ru.clevertec.service.dto.NewsToPostDto;

@Mapper(uses = CommentMapper.class)
public interface NewsMapper {

    @Mapping(target = "comments", ignore = true)
    NewsDto toDto(News entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    NewsDto toDtoWithOutIdAndComments(News entity);

    News postNewsToEntity(NewsToPostDto newsToPostDto);

}
