package ru.clevertec.service.dto;

import lombok.Value;
import org.springframework.data.domain.Page;

@Value
public class NewsWithCommentsResponse {

    NewsDto news;
    PageResponse<CommentDto> comments;

    public static NewsWithCommentsResponse of(Page<CommentDto> commentDto, NewsDto news) {
        return new NewsWithCommentsResponse(news, PageResponse.of(commentDto));
    }
}
