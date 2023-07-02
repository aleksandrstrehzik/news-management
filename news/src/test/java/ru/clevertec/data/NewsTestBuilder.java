package ru.clevertec.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.repository.entity.Comment;
import ru.clevertec.repository.entity.News;
import ru.clevertec.service.dto.CommentDto;
import ru.clevertec.service.dto.NewsDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@With
@Getter
@AllArgsConstructor
@NoArgsConstructor(staticName = "aNews")
public class NewsTestBuilder implements TestBuilder<News>, TestDtoBuilder<NewsDto> {

    private Long id = 1000L;
    private Instant time = Instant.now();
    private String createBy = "TestUser";
    private String title = "Test title";
    private String text = "Test text";
    private List<Comment> comments = null;
    private List<CommentDto> commentsDto = null;

    @Override
    public News build() {
        final News news = new News();
        news.setId(id);
        news.setTime(time);
        news.setCreateBy(createBy);
        news.setTitle(title);
        news.setText(text);
        news.setComments(new ArrayList<>());
        return news;
    }

    @Override
    public NewsDto buildDto() {
        final NewsDto newsDto = new NewsDto();
        newsDto.setId(id);
        newsDto.setTime(time);
        newsDto.setCreateBy(createBy);
        newsDto.setTitle(title);
        newsDto.setText(text);
        newsDto.setComments(commentsDto);
        return newsDto;
    }
}
