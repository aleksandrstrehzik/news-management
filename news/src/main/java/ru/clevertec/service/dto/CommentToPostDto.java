package ru.clevertec.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentToPostDto {

    @NotBlank(message = "Text can not be blank")
    @Size(max = 200, message = "Text can not be greater than 200 symbols")
    private String text;

    private NewsDto news;
}
