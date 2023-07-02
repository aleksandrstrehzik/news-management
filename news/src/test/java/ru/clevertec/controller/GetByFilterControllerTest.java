package ru.clevertec.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.integration.BaseIntegrationTest;
import ru.clevertec.service.specifications.CommentFilter;
import ru.clevertec.service.specifications.NewsFilter;

import java.time.Instant;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class GetByFilterControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final String URI = "/api/v1/getByFilter";

    @Test
    void getAllNewsByFilter() throws Exception {
        NewsFilter filter = NewsFilter.builder()
                .createBy("Gena")
                .build();
        String filterJSON = objectMapper.writeValueAsString(filter);

        this.mockMvc.perform(get(URI + "/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterJSON))
                .andExpect(content().string(containsString("\"metadata\":{\"page\":0,\"size\":3,\"totalElements\":2}")));
    }

    @Test
    void getAllCommentsByFilter() throws Exception {
        CommentFilter filter = CommentFilter.builder()
                  .time(Instant.parse("2022-04-24T11:54:27.613463Z"))
                .build();
        String filterJSON = objectMapper.writeValueAsString(filter);

        this.mockMvc.perform(get(URI + "/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterJSON))
                .andExpect(content().string(containsString("\"metadata\":{\"page\":0,\"size\":3,\"totalElements\":69}")));
    }
}