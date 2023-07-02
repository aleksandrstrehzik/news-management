package ru.clevertec.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.service.dto.CommentWithUpdateDto;

@With
@Getter
@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentWithUpdate")
public class CommentWithUpdateTestBuilder implements TestDtoBuilder<CommentWithUpdateDto>{

    private String id = "1000";
    private String text = "Test text";

    @Override
    public CommentWithUpdateDto buildDto() {
        final CommentWithUpdateDto commentWithUpdateDto = new CommentWithUpdateDto();
        commentWithUpdateDto.setId(id);
        commentWithUpdateDto.setText(text);
        return commentWithUpdateDto;
    }
}
