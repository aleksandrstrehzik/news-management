package ru.clevertec.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.service.dto.CommentToPostDto;
import ru.clevertec.service.dto.NewsDto;

@With
@Getter
@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentToPost")
public class CommentToPostTestBuilder implements TestDtoBuilder<CommentToPostDto> {

    private String text = "Test text";
    private NewsDto news = null;

    @Override
    public CommentToPostDto buildDto() {
        final CommentToPostDto commentToPostDto = new CommentToPostDto();
        commentToPostDto.setText(text);
        commentToPostDto.setNews(null);
        return commentToPostDto;
    }
}
