package ru.clevertec.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.data.*;
import ru.clevertec.integration.BaseIntegrationTest;
import ru.clevertec.service.dto.*;

import java.time.Instant;
import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class NewsCrudControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final String URI = "/api/v1/news";

    @Test
    void getById() throws Exception {
        NewsDto newsDto = NewsTestBuilder.aNews()
                .withText("Хакеры из скандально известной группы Anonymous заявили, что смогли получить доступ к исходным кодам ядра популярной операционной системы Linux, взломав внутреннюю сеть дата-центра в Сан-Франциско. В качестве подтверждения злоумышленники разместили часть исходников Linux на сайте The Pirate Bay.\n" +
                        "Факт утечки уже признал владелец торговой марки Linux Линус Торвальдс. Через считанные часы после публикации magnet-ссылки, позволяющей скачать исходный код, он написал твит, в котором предостерёг от скачивания нелегально полученных файлов.")
                .withTime(Instant.parse("2023-04-24T14:34:27.613463Z"))
                .withTitle("Хакеры украли исходные коды ядра Linux: Линус Торвальдс подтвердил утечку")
                .withId(14L)
                .withCreateBy("Gena")
                .withCommentsDto(null)
                .buildDto();
        String newsJson = objectMapper.writeValueAsString(newsDto);

        this.mockMvc.perform(get(URI + "/{id}", 14))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(newsJson));
    }

    @Test
    @WithUserDetails("Vasia")
    void deleteByIdByAdmin() throws Exception {
        this.mockMvc.perform(delete(URI + "/{id}", 1))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    @WithUserDetails("Vale")
    void deleteByIdBySubscriber() throws Exception {
        this.mockMvc.perform(delete(URI + "/{id}", 3))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("Vaia")
    void deleteByIdByJournalistWitchPostNews() throws Exception {
        this.mockMvc.perform(delete(URI + "/{id}", 19))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    @WithUserDetails("Vaia")
    void deleteByIdByJournalistWitchNotLeaveWithComment() throws Exception {
        this.mockMvc.perform(delete(URI + "/{id}", 6))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void postByUnauthenticated() throws Exception {
        NewsToPostDto newsToPostDto = NewsToPostTestBuilder.aCommentToPost()
                .withText("test")
                .withTitle("test")
                .buildDto();
        String newsToPostDtoJSON = objectMapper.writeValueAsString(newsToPostDto);

        this.mockMvc.perform(post(URI).
                        contentType(MediaType.APPLICATION_JSON).
                        content(newsToPostDtoJSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("Vsia")
    void postByJournalist() throws Exception {
        NewsToPostDto newsToPostDto = NewsToPostTestBuilder.aCommentToPost()
                .withText("test")
                .withTitle("test")
                .buildDto();
        String newsToPostDtoJSON = objectMapper.writeValueAsString(newsToPostDto);

        this.mockMvc.perform(post(URI).
                        contentType(MediaType.APPLICATION_JSON).
                        content(newsToPostDtoJSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(newsToPostDto.getText())))
                .andExpect(content().string(containsString(newsToPostDto.getTitle())))
                .andExpect(content().string(containsString("\"createBy\":\"Vsia\"")));
    }

    @Test
    @WithUserDetails("Vasia")
    void postByAdmin() throws Exception {
        NewsToPostDto newsToPostDto = NewsToPostTestBuilder.aCommentToPost()
                .withText("test")
                .withTitle("test")
                .buildDto();
        String newsToPostDtoJSON = objectMapper.writeValueAsString(newsToPostDto);

        this.mockMvc.perform(post(URI).
                        contentType(MediaType.APPLICATION_JSON).
                        content(newsToPostDtoJSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(newsToPostDto.getText())))
                .andExpect(content().string(containsString(newsToPostDto.getTitle())))
                .andExpect(content().string(containsString("\"createBy\":\"Vasia\"")));
    }

    @Test
    @WithUserDetails("Vale")
    void postBySubscriber() throws Exception {
        NewsToPostDto newsToPostDto = NewsToPostTestBuilder.aCommentToPost()
                .withText("test")
                .withTitle("test")
                .buildDto();
        String newsToPostDtoJSON = objectMapper.writeValueAsString(newsToPostDto);

        this.mockMvc.perform(post(URI).
                        contentType(MediaType.APPLICATION_JSON).
                        content(newsToPostDtoJSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("Vasia")
    void updateByAdmin() throws Exception {
        NewsWithUpdateDto newsWithUpdateDto = NewsWithUpdateTestBuilder.aNewsWithUpdate()
                .withId("1")
                .withText("test")
                .withText("test")
                .buildDto();
        String newsWithUpdateDtoJSON = objectMapper.writeValueAsString(newsWithUpdateDto);

        this.mockMvc.perform(patch(URI).
                        contentType(MediaType.APPLICATION_JSON).
                        content(newsWithUpdateDtoJSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString(newsWithUpdateDto.getText())))
                .andExpect(content().string(containsString(newsWithUpdateDto.getId())))
                .andExpect(content().string(containsString(newsWithUpdateDto.getTitle())));
    }

    @Test
    @WithUserDetails("Vaia")
    void updateByJournalistWitchCreateNews() throws Exception {
        NewsWithUpdateDto newsWithUpdateDto = NewsWithUpdateTestBuilder.aNewsWithUpdate()
                .withId("20")
                .withText("test")
                .withText("test")
                .buildDto();
        String newsWithUpdateDtoJSON = objectMapper.writeValueAsString(newsWithUpdateDto);

        this.mockMvc.perform(patch(URI).
                        contentType(MediaType.APPLICATION_JSON).
                        content(newsWithUpdateDtoJSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString(newsWithUpdateDto.getText())))
                .andExpect(content().string(containsString(newsWithUpdateDto.getId())))
                .andExpect(content().string(containsString(newsWithUpdateDto.getTitle())))
                .andExpect(content().string(containsString("\"createBy\":\"Vaia\"")));
    }

    @Test
    @WithUserDetails("Vsia")
    void updateByJournalistWitchNotCreateComment() throws Exception {
        NewsWithUpdateDto newsWithUpdateDto = NewsWithUpdateTestBuilder.aNewsWithUpdate()
                .withId("20")
                .withText("test")
                .withText("test")
                .buildDto();
        String newsWithUpdateDtoJSON = objectMapper.writeValueAsString(newsWithUpdateDto);

        this.mockMvc.perform(patch(URI).
                        contentType(MediaType.APPLICATION_JSON).
                        content(newsWithUpdateDtoJSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    void findAll() throws Exception {
        this.mockMvc.perform(get(URI))
                .andDo(print())
                .andExpect(content().string(containsString("\"metadata\":{\"page\":0,\"size\":5,\"totalElements\":20}")));
    }
}