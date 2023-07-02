package ru.clevertec.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.data.NewsTestBuilder;
import ru.clevertec.data.NewsToPostTestBuilder;
import ru.clevertec.data.NewsWithUpdateTestBuilder;
import ru.clevertec.exceptionhandler.exceptions.entitynotfound.NewsNotFoundException;
import ru.clevertec.repository.NewsRepository;
import ru.clevertec.repository.entity.News;
import ru.clevertec.service.dto.NewsDto;
import ru.clevertec.service.dto.NewsToPostDto;
import ru.clevertec.service.dto.NewsWithUpdateDto;
import ru.clevertec.service.mappers.NewsMapper;
import ru.clevertec.service.specifications.NewsFilter;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class NewsCrudServiceImplTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private NewsCrudServiceImpl newsCrudService;

    @Test
    void save() {
        NewsToPostDto newsToPostDto = NewsToPostTestBuilder.aCommentToPost().buildDto();
        News news = NewsTestBuilder.aNews().build();
        NewsDto expected = NewsTestBuilder.aNews().buildDto();

        Mockito.doReturn(news).when(newsMapper)
                .postNewsToEntity(newsToPostDto);
        Mockito.doReturn(expected).when(newsMapper)
                .toDto(news);
        Mockito.doReturn(news).when(newsRepository)
                .save(news);

        NewsDto actual = newsCrudService.save(newsToPostDto);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("ru.clevertec.service.impl.CommentCrudServiceImplTest#getId")
    void delete(Long id) {
        newsCrudService.delete(id);

        Mockito.verify(newsRepository).deleteById(id);
    }

    @Nested
    class UpdateTest {

        @ParameterizedTest
        @MethodSource("ru.clevertec.service.impl.CommentCrudServiceImplTest#getText")
        void successfullyUpdate(String updateText, String updateTitle) {
            NewsWithUpdateDto newsWithUpdateDto = NewsWithUpdateTestBuilder.aNewsWithUpdate()
                    .withText(updateText).withTitle(updateTitle).buildDto();
            NewsDto expected = NewsTestBuilder.aNews().buildDto();
            News news = NewsTestBuilder.aNews().build();

            Mockito.doReturn(Optional.of(news)).when(newsRepository)
                    .findById(Long.parseLong(newsWithUpdateDto.getId()));
            Mockito.doReturn(news).when(newsRepository)
                    .save(news);
            Mockito.doReturn(expected).when(newsMapper).toDto(news);

            NewsDto actual = newsCrudService.update(newsWithUpdateDto);

            assertThat(news.getText()).isEqualTo(newsWithUpdateDto.getText());
            assertThat(news.getTitle()).isEqualTo(newsWithUpdateDto.getTitle());
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @MethodSource("ru.clevertec.service.impl.CommentCrudServiceImplTest#getId")
        void shouldThrowNewsNotFoundExceptionUpdate(Long id) {
            NewsWithUpdateDto newsWithUpdateDto = NewsWithUpdateTestBuilder.aNewsWithUpdate()
                    .withId(id.toString()).buildDto();
            String expected = "No news with id = " + id + " found";

            Mockito.doReturn(Optional.empty()).when(newsRepository)
                    .findById(Long.parseLong(newsWithUpdateDto.getId()));

            NewsNotFoundException exception = assertThrows(NewsNotFoundException.class,
                    () -> newsCrudService.update(newsWithUpdateDto));
            String actual = exception.getMessage();
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class FindByIdTest {

        @ParameterizedTest
        @MethodSource("ru.clevertec.service.impl.CommentCrudServiceImplTest#getId")
        void successfullyFindById(Long id) {
            News news = NewsTestBuilder.aNews().withId(id).build();
            NewsDto expected = NewsTestBuilder.aNews().buildDto();

            Mockito.doReturn(Optional.of(news)).when(newsRepository)
                    .findById(id);
            Mockito.doReturn(expected).when(newsMapper)
                    .toDto(news);

            NewsDto actual = newsCrudService.findById(id);

            assertThat(actual).isEqualTo(expected);
        }


        @ParameterizedTest
        @MethodSource("ru.clevertec.service.impl.CommentCrudServiceImplTest#getId")
        void shouldReturnFindById(Long id) {
            String expected = "No news with id = " + id + " found";

            Mockito.doReturn(Optional.empty()).when(newsRepository)
                    .findById(id);

            NewsNotFoundException exception = assertThrows(NewsNotFoundException.class,
                    () -> newsCrudService.findById(id));
            String actual = exception.getMessage();
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Test
    void findAll() {
        News news1 = NewsTestBuilder.aNews().withId(1L).build();
        News news2 = NewsTestBuilder.aNews().withId(2L).build();
        News news3 = NewsTestBuilder.aNews().withId(3L).build();
        NewsDto newsDto1 = NewsTestBuilder.aNews().buildDto();
        NewsDto newsDto2 = NewsTestBuilder.aNews().buildDto();
        NewsDto newsDto3 = NewsTestBuilder.aNews().buildDto();

        List<News> newsList = List.of(news1, news2, news3);
        List<NewsDto> newsDtoList = List.of(newsDto1, newsDto2, newsDto3);

        Page<News> newsPage = new PageImpl<>(newsList);
        Page<NewsDto> expected = new PageImpl<>(newsDtoList);

        Pageable request = PageRequest.of(0, 3);

        Mockito.doReturn(newsDto1).when(newsMapper)
                .toDtoWithOutIdAndComments(news1);
        Mockito.doReturn(newsDto2).when(newsMapper)
                .toDtoWithOutIdAndComments(news2);
        Mockito.doReturn(newsDto3).when(newsMapper)
                .toDtoWithOutIdAndComments(news3);
        Mockito.doReturn(newsPage).when(newsRepository)
                .findAll(request);

        Page<NewsDto> actual = newsCrudService.findAll(request);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getByFilter() {
        News news1 = NewsTestBuilder.aNews().withId(1L).build();
        News news2 = NewsTestBuilder.aNews().withId(2L).build();
        News news3 = NewsTestBuilder.aNews().withId(3L).build();
        NewsDto newsDto1 = NewsTestBuilder.aNews().buildDto();
        NewsDto newsDto2 = NewsTestBuilder.aNews().buildDto();
        NewsDto newsDto3 = NewsTestBuilder.aNews().buildDto();
        NewsFilter newsFilter = new NewsFilter("z", "z","z", Instant.now());

        List<News> newsList = List.of(news1, news2, news3);
        List<NewsDto> newsDtoList = List.of(newsDto1, newsDto2, newsDto3);

        Page<News> newsPage = new PageImpl<>(newsList);
        Page<NewsDto> expected = new PageImpl<>(newsDtoList);

        Pageable request = PageRequest.of(0, 20);

        Mockito.doReturn(newsDto1).when(newsMapper)
                .toDtoWithOutIdAndComments(news1);
        Mockito.doReturn(newsDto2).when(newsMapper)
                .toDtoWithOutIdAndComments(news2);
        Mockito.doReturn(newsDto3).when(newsMapper)
                .toDtoWithOutIdAndComments(news3);
        Mockito.doReturn(newsPage).when(newsRepository)
                .findAll((Specification<News>) Mockito.any(), Mockito.any());

        Page<NewsDto> actual = newsCrudService.getByFilter(newsFilter, request);

        assertThat(actual).isEqualTo(expected);
     }
}
