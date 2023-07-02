package ru.clevertec.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.integration.BaseIntegrationTest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class NewsWithCommentControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final String URI = "/api/v1/newsWithComments";

    @Test
    void getByIdNewsWithComments() throws Exception {
        this.mockMvc.perform(get(URI + "/{id}", 14))
                .andDo(print())
                .andExpect(content().string(containsString("\"id\":14")))
                .andExpect(content().string(containsString("\"time\":\"2023-04-24T14:34:27.613463Z\"")))
              //  .andExpect(content().string(containsString("\"title\":\"Хакеры украли исходные коды ядра Linux: Линус Торвальдс подтвердил утечку\"")))
                .andExpect(content().string(containsString("\"createBy\":\"Gena\"")))
                .andExpect(content().string(containsString("\"metadata\":{\"page\":0,\"size\":3,\"totalElements\":10}")));
    }
}