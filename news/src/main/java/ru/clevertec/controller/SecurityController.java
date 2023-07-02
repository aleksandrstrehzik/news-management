package ru.clevertec.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.controller.feign.UserFeignController;
import ru.clevertec.exceptionhandler.exceptions.entitynotfound.UserNotFoundException;
import ru.clevertec.exceptionhandler.exceptions.validation.EntityValidationException;
import ru.clevertec.logging.annotations.BeforeLog;
import ru.clevertec.service.dto.LoginForm;
import ru.clevertec.service.dto.UserDto;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Tag(name = "Security Controller", description = "Methods for login and logout")
public class SecurityController {

    private final UserFeignController userFeignController;
    private final PasswordEncoder passwordEncoder;

    /**
     * Метод для аутенфикации пользователя
     *
     * @param form Объект класса LoginForm содержащий пароль и имя пользователя
     * @return Объект класса ResponseEntity содержащий строку с приветствием и статус ответа
     */
    @BeforeLog
    @PostMapping("/login")
    @Operation(summary = "Authorizes user")
    public ResponseEntity<String> login(@RequestBody @Validated
                                                LoginForm form,
                                        @Parameter(hidden = true)
                                                BindingResult bindingResult,
                                        @Parameter(hidden = true)
                                                HttpServletRequest request) throws ServletException {
        if (bindingResult.hasErrors()) {
            throw new EntityValidationException(bindingResult);
        }
        UserDto user = userFeignController.getUserByName(form.getUsername()).getBody();
        if (user.getUsername().equals(form.getUsername()) &&
                passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            request.login(form.getUsername(), form.getPassword());
            Principal principal = request.getUserPrincipal();
            return ResponseEntity.ok("User " + principal.getName() + " successfully authenticated");
        } else throw new UserNotFoundException("No such user");
    }

    /**
     * Метод для выхода пользователя пользователя
     *
     * @return Объект класса ResponseEntity содержащий строку с прощанием и статус ответа
     */
    @BeforeLog
    @PostMapping("/logout-r")
    @Operation(summary = "Authorizes user")
    public ResponseEntity<String> logout(@Parameter(hidden = true)
                                                 HttpServletRequest request) throws ServletException {
        if (request.getUserPrincipal() == null) {
            throw new UserNotFoundException("Your are not authenticate yet");
        }
        request.logout();
        return ResponseEntity.ok("Successfully logout");
    }

}
