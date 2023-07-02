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
public class CommentWithUpdateDto {

    @NotBlank(message = "Id can not be null")
    @Digits(message = "Id should be integer", integer = 19, fraction = 0)
    @Positive(message = "Id can not be negative")
    private String id;

    @NotBlank(message = "Text can not be blank")
    @Size(max = 200, message = "Text can not be greater than 200 symbols")
    private String text;

}
