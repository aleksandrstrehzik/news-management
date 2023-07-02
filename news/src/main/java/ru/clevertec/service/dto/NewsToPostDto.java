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
public class NewsToPostDto {

    @NotBlank(message = "Title can not be blank")
    @Size(max = 200, message = "Title can not be greater than 200 symbols")
    private String title;

    @NotBlank(message = "Text can not be blank")
    private String text;
}
