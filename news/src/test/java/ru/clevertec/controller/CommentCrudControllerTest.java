package ru.clevertec.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.data.CommentTestBuilder;
import ru.clevertec.data.CommentToPostTestBuilder;
import ru.clevertec.data.CommentWithUpdateTestBuilder;
import ru.clevertec.integration.BaseIntegrationTest;
import ru.clevertec.service.dto.CommentDto;
import ru.clevertec.service.dto.CommentToPostDto;
import ru.clevertec.service.dto.CommentWithUpdateDto;

import java.time.Instant;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CommentCrudControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final String URI = "/api/v1/comment";

    @Test
    void getById() throws Exception {
        CommentDto commentDto = CommentTestBuilder.aComment().withId(1L)
                .withText("Зюганов больше не купится на это, зря стараетесь.")
                .withNews(null)
                .withTime(Instant.parse("2021-05-21T15:24:28.653463Z"))
                .withUsername("Dickarat Heckingbottomvale").buildDto();
        String commentJson = objectMapper.writeValueAsString(commentDto);

        this.mockMvc.perform(get(URI + "/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(commentJson));
    }

    @Test
    @WithUserDetails("Vasia")
    void deleteByIdByAdmin() throws Exception {
        this.mockMvc.perform(delete(URI + "/{id}", 2))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    @WithUserDetails("Vsia")
    void deleteByIdByJournalist() throws Exception {
        this.mockMvc.perform(delete(URI + "/{id}", 3))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("Vale")
    void deleteByIdBySubscriberWitchLeaveWithComment() throws Exception {
        this.mockMvc.perform(delete(URI + "/{id}", 7))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    @WithUserDetails("Vale")
    void deleteByIdBySubscriberWitchNotLeaveWithComment() throws Exception {
        this.mockMvc.perform(delete(URI + "/{id}", 6))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void postByUnauthenticated() throws Exception {
        CommentToPostDto commentToPostDto = CommentToPostTestBuilder.aCommentToPost().buildDto();
        String commentToPostJson = objectMapper.writeValueAsString(commentToPostDto);

        this.mockMvc.perform(post(URI + "/{newsId}", 1).
                        contentType(MediaType.APPLICATION_JSON).
                        content(commentToPostJson))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("Vsia")
    void postByJournalist() throws Exception {
        CommentToPostDto commentToPostDto = CommentToPostTestBuilder.aCommentToPost().buildDto();
        String commentToPostJson = objectMapper.writeValueAsString(commentToPostDto);

        this.mockMvc.perform(post(URI + "/{newsId}", 1).
                        contentType(MediaType.APPLICATION_JSON).
                        content(commentToPostJson))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("Vasia")
    void postByAdmin() throws Exception {
        CommentToPostDto commentToPostDto = CommentToPostTestBuilder.aCommentToPost().buildDto();
        String commentToPostJson = objectMapper.writeValueAsString(commentToPostDto);

        this.mockMvc.perform(post(URI + "/{newsId}", 1).
                        contentType(MediaType.APPLICATION_JSON).
                        content(commentToPostJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(commentToPostDto.getText())));
    }

    @Test
    @WithUserDetails("Vale")
    void postBySubscriber() throws Exception {
        CommentToPostDto commentToPostDto = CommentToPostTestBuilder.aCommentToPost().buildDto();
        String commentToPostJson = objectMapper.writeValueAsString(commentToPostDto);

        this.mockMvc.perform(post(URI + "/{newsId}", 1).
                        contentType(MediaType.APPLICATION_JSON).
                        content(commentToPostJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(commentToPostDto.getText())));
    }

    @Test
    @WithUserDetails("Vasia")
    void updateByAdmin() throws Exception {
        CommentWithUpdateDto commentWithUpdateDto = CommentWithUpdateTestBuilder.aCommentWithUpdate()
                .withId("12")
                .withText("Update text for test").buildDto();
        String commentWithUpdateDtoJSON = objectMapper.writeValueAsString(commentWithUpdateDto);

        this.mockMvc.perform(patch(URI).
                        contentType(MediaType.APPLICATION_JSON).
                        content(commentWithUpdateDtoJSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString(commentWithUpdateDto.getText())));
    }

    @Test
    @WithUserDetails("Vale")
    void updateBySubscriberWitchCreateComment() throws Exception {
        CommentWithUpdateDto commentWithUpdateDto = CommentWithUpdateTestBuilder.aCommentWithUpdate()
                .withId("15")
                .withText("Update text for test").buildDto();
        String commentWithUpdateDtoJSON = objectMapper.writeValueAsString(commentWithUpdateDto);

        this.mockMvc.perform(patch(URI).
                        contentType(MediaType.APPLICATION_JSON).
                        content(commentWithUpdateDtoJSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString(commentWithUpdateDto.getText())));
    }
}
