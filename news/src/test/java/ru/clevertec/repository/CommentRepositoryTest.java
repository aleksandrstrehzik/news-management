package ru.clevertec.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestConstructor;
import ru.clevertec.integration.BaseIntegrationTest;
import ru.clevertec.repository.entity.Comment;
import ru.clevertec.service.specifications.CommentFilter;
import ru.clevertec.service.specifications.NewsFilter;
import ru.clevertec.service.specifications.Specifications;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CommentRepositoryTest extends BaseIntegrationTest {

    private final CommentRepository commentRepository;

    @Test
    void checkFindCommentByNewsId() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Comment> content = commentRepository.findByNews_Id(1L, pageable).getContent();

        assertThat(content.get(0).getUsername()).isEqualTo("Dickarat Heckingbottomvale");
        assertThat(content.get(1).getUsername()).isEqualTo("Valentina Kosareva");
        assertThat(content.get(2).getUsername()).isEqualTo("Sergey");
        assertThat(content.get(3).getUsername()).isEqualTo("Душа Нила");
        assertThat(content.get(4).getUsername()).isEqualTo("Марина Я Марина");
    }

    @Test
    void checkFindCommentByNewsIdShouldBeEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Comment> content = commentRepository.findByNews_Id(1000L, pageable).getContent();

        assertThat(content).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("getDate")
    void checkFindAllShouldReturnCommentWithDateAfterEntering(String date) {
        Pageable pageable = PageRequest.of(0, 200);
        CommentFilter commentFilter = CommentFilter.builder()
                .time(Instant.parse(date))
                .build();
        Specification<Comment> specification = Specifications.getCommentSpecification(commentFilter);

        Optional<Comment> any = commentRepository.findAll(specification, pageable).getContent().stream()
                .filter(comment -> comment.getTime().isBefore(commentFilter.getTime()))
                .findAny();

        assertThat(any).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("getText")
    void checkFindAllShouldReturnCommentWithWordsInText(String text) {
        Pageable pageable = PageRequest.of(0, 200);
        CommentFilter commentFilter = CommentFilter.builder()
                .text(text)
                .build();
        Specification<Comment> specification = Specifications.getCommentSpecification(commentFilter);

        List<Comment> content = commentRepository.findAll(specification, pageable).getContent();
        long expected = content.stream()
                .peek(System.out::println)
                .filter(comment -> comment.getText().contains(text))
                .count();

        assertThat(content.size()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("getUsername")
    void checkFindAllShouldReturnCommentByUser(String username) {
        Pageable pageable = PageRequest.of(0, 200);
        CommentFilter commentFilter = CommentFilter.builder()
                .username(username)
                .build();
        Specification<Comment> specification = Specifications.getCommentSpecification(commentFilter);

        List<Comment> content = commentRepository.findAll(specification, pageable).getContent();
        long expected = content.stream()
                .filter(comment -> comment.getUsername().contains(username))
                .count();

        assertThat(content.size()).isEqualTo(expected);
    }

    static Stream<Arguments> getDate() {
        return Stream.of(
                arguments("2021-03-08T14:44:27.613464Z"),
                arguments("2022-03-08T14:44:27.613464Z"),
                arguments("2023-03-08T14:44:27.613464Z"));
    }

    static Stream<Arguments> getText() {
        return Stream.of(
                arguments("город"),
                arguments("В"),
                arguments("Нашли применение"));
    }

    static Stream<Arguments> getUsername() {
        return Stream.of(
                arguments("Macmep"),
                arguments("Lazoryak"),
                arguments("Vladimir Popov"));
    }
}