package ru.clevertec.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestConstructor;
import ru.clevertec.integration.BaseIntegrationTest;
import ru.clevertec.repository.entity.Comment;
import ru.clevertec.repository.entity.News;
import ru.clevertec.service.specifications.CommentFilter;
import ru.clevertec.service.specifications.NewsFilter;
import ru.clevertec.service.specifications.Specifications;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class NewsRepositoryTest extends BaseIntegrationTest {

    private final NewsRepository newsRepository;

    @ParameterizedTest
    @MethodSource("ru.clevertec.repository.CommentRepositoryTest#getDate")
    void checkFindAllShouldReturnCommentWithDateAfterEntering(String date) {
        Pageable pageable = PageRequest.of(0, 200);
        NewsFilter newsFilter = NewsFilter.builder()
                .time(Instant.parse(date))
                .build();
        Specification<News> specification = Specifications.getNewsSpecification(newsFilter);

        Optional<News> any = newsRepository.findAll(specification, pageable).getContent().stream()
                .filter(news -> news.getTime().isBefore(newsFilter.getTime()))
                .findAny();

        assertThat(any).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("ru.clevertec.repository.CommentRepositoryTest#getText")
    void checkFindAllShouldReturnCommentWithWordsInText(String text) {
        Pageable pageable = PageRequest.of(0, 200);
        NewsFilter newsFilter = NewsFilter.builder()
                .text(text)
                .build();
        Specification<News> specification = Specifications.getNewsSpecification(newsFilter);

        List<News> content = newsRepository.findAll(specification, pageable).getContent();
        long expected = content.stream()
                .filter(news -> news.getText().contains(text))
                .count();

        assertThat(content.size()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("getCreateBy")
    void checkFindAllShouldReturnCommentByUser(String createBy) {
        Pageable pageable = PageRequest.of(0, 200);
        NewsFilter newsFilter = NewsFilter.builder()
                .createBy(createBy)
                .build();
        Specification<News> specification = Specifications.getNewsSpecification(newsFilter);

        List<News> content = newsRepository.findAll(specification, pageable).getContent();
        long expected = content.stream()
                .filter(news -> news.getCreateBy().contains(createBy))
                .count();

        assertThat(content.size()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("getTitle")
    void checkFindAllShouldReturnCommentWithWordsInTitle(String title) {
        Pageable pageable = PageRequest.of(0, 200);
        NewsFilter newsFilter = NewsFilter.builder()
                .title(title)
                .build();
        Specification<News> specification = Specifications.getNewsSpecification(newsFilter);

        List<News> content = newsRepository.findAll(specification, pageable).getContent();
        long expected = content.stream()
                .filter(news -> news.getTitle().contains(title))
                .count();

        assertThat(content.size()).isEqualTo(expected);
    }

    static Stream<Arguments> getCreateBy() {
        return Stream.of(
                arguments("Mike"),
                arguments("Саханков"),
                arguments("Черепанов"));
    }

    static Stream<Arguments> getTitle() {
        return Stream.of(
                arguments("Канаде"),
                arguments("Россия"),
                arguments("края"));
    }
}