package ru.clevertec.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.service.dto.NewsToPostDto;

@With
@Getter
@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentToPost")
public class NewsToPostTestBuilder implements TestDtoBuilder<NewsToPostDto> {

    private String text = "Test text";
    private String title = "Test title";

    @Override
    public NewsToPostDto buildDto() {
        final NewsToPostDto newsToPostDto = new NewsToPostDto();
        newsToPostDto.setText(text);
        newsToPostDto.setTitle(title);
        return newsToPostDto;
    }
}
