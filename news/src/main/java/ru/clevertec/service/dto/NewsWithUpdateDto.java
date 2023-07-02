package ru.clevertec.service.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewsWithUpdateDto {

    @NotBlank
    @Digits(message = "id should be digit", integer = 19, fraction = 0)
    @Positive(message = "Id can not be negative")
    private String id;

    @Size(max = 200, message = "Title can not be greater than 200 symbols")
    private String title;

    @NotBlank(message = "Text can not be blank")
    private String text;
}
