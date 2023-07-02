package ru.clevertec.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.bouncycastle.crypto.engines.SEEDWrapEngine;
import ru.clevertec.repository.entity.Comment;
import ru.clevertec.repository.entity.News;
import ru.clevertec.service.dto.CommentDto;
import ru.clevertec.service.dto.NewsDto;

import java.time.Instant;

@With
@Getter
@AllArgsConstructor
@NoArgsConstructor(staticName = "aComment")
public class CommentTestBuilder implements TestBuilder<Comment>, TestDtoBuilder<CommentDto> {

    private Long id = 1000L;
    private Instant time = Instant.now();
    private String username = "Test username";
    private String text = "Test Text";
    private NewsDto news = null;

    @Override
    public Comment build() {
        final Comment comment = new Comment();
        comment.setId(id);
        comment.setTime(time);
        comment.setUsername(username);
        comment.setText(text);
        comment.setNews(null);
        return comment;
    }

    @Override
    public CommentDto buildDto() {
        final CommentDto commentDto = new CommentDto();
        commentDto.setTime(time);
        commentDto.setUsername(username);
        commentDto.setText(text);
        commentDto.setNews(null);
        return commentDto;
    }
}
