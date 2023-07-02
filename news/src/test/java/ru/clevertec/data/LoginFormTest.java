package ru.clevertec.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.repository.entity.roles.Role;
import ru.clevertec.service.dto.LoginForm;

@With
@Getter
@AllArgsConstructor
@NoArgsConstructor(staticName = "aLoginForm")
public class LoginFormTest implements TestBuilder<LoginForm>{

    private String username = "Vasia";
    private String password = "1";

    @Override
    public LoginForm build() {
        return LoginForm.builder()
                .username(username)
                .password(password)
                .build();
    }
}
