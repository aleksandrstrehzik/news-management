package ru.clevertec.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.data.LoginFormTest;
import ru.clevertec.integration.BaseIntegrationTest;
import ru.clevertec.service.dto.LoginForm;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SecurityControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void loginSuccessfully() throws Exception {
        LoginForm loginForm = LoginFormTest.aLoginForm().build();
        String valueAsString = objectMapper.writeValueAsString(loginForm);
        this.mockMvc.perform(post("/login").
                        contentType(MediaType.APPLICATION_JSON).
                        content(valueAsString))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("User "+ loginForm.getUsername()+ " successfully authenticated"));
    }

    @Test
    void loginBadCredentials() throws Exception {
        LoginFormTest loginFormTest = LoginFormTest.aLoginForm().withUsername("bad request");
        String valueAsString = objectMapper.writeValueAsString(loginFormTest);
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valueAsString))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
