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
public class LoginForm {

    @NotBlank(message = "Text can not be blank")
    @Size(max = 50, message = "Title can not be greater than 50 symbols")
    private String username;

    @NotBlank(message = "Text can not be blank")
    private String password;
}
