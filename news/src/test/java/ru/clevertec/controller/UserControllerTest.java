package ru.clevertec.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.integration.BaseIntegrationTest;
import ru.clevertec.repository.entity.roles.Role;
import ru.clevertec.service.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final String URI = "/api/v1/user";

    @Test
    void getUser() throws Exception {
        UserDto userDto = UserDto.builder()
                .username("Vasia")
                .password("$2a$10$uZ/KPLUvV6u4HgOVBmLBquYc3eCFrJncn/lglB02Sgj9.WehyDePS")
                .role(Role.ADMIN)
                .build();
        String userDtoJSON = objectMapper.writeValueAsString(userDto);
        this.mockMvc.perform(get(URI + "/{name}", "Vasia"))
                .andExpect(content().json(userDtoJSON));
    }
}