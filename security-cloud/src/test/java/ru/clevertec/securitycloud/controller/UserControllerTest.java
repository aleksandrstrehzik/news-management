package ru.clevertec.securitycloud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.securitycloud.integration.BaseIntegrationTest;
import ru.clevertec.securitycloud.repository.entity.roles.Role;
import ru.clevertec.securitycloud.service.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final String URI = "/api/v1/user";

    @Test
    void getUserByName() throws Exception {
        UserDto expected = UserDto.builder()
                .username("Vasia")
                .password("1")
                .role(Role.ADMIN)
                .build();
        String expectedJSON = objectMapper.writeValueAsString(expected);

        this.mockMvc.perform(get(URI + "/{name}", "Vasia"))
                .andDo(print())
                .andExpect(content().json(expectedJSON));
    }
}