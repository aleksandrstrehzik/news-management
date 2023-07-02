package ru.clevertec.data;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.service.dto.NewsWithUpdateDto;

@With
@Getter
@AllArgsConstructor
@NoArgsConstructor(staticName = "aNewsWithUpdate")
public class NewsWithUpdateTestBuilder implements TestDtoBuilder<NewsWithUpdateDto>{

    private String id = "1000";
    private String title = "Test title";
    private String text = "Test text";

    @Override
    public NewsWithUpdateDto buildDto() {
        final NewsWithUpdateDto newsWithUpdateDto = new NewsWithUpdateDto();
        newsWithUpdateDto.setId(id);
        newsWithUpdateDto.setTitle(title);
        newsWithUpdateDto.setText(text);
        return newsWithUpdateDto;
    }
}
