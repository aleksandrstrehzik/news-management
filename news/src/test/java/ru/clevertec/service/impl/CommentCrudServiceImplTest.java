package ru.clevertec.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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
import ru.clevertec.data.CommentTestBuilder;
import ru.clevertec.data.CommentToPostTestBuilder;
import ru.clevertec.data.CommentWithUpdateTestBuilder;
import ru.clevertec.data.NewsTestBuilder;
import ru.clevertec.exceptionhandler.exceptions.entitynotfound.CommentNotFoundException;
import ru.clevertec.repository.CommentRepository;
import ru.clevertec.repository.entity.Comment;
import ru.clevertec.service.dto.CommentDto;
import ru.clevertec.service.dto.CommentToPostDto;
import ru.clevertec.service.dto.CommentWithUpdateDto;
import ru.clevertec.service.dto.NewsDto;
import ru.clevertec.service.interfaces.NewsCrudService;
import ru.clevertec.service.mappers.CommentMapper;
import ru.clevertec.service.specifications.CommentFilter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(MockitoExtension.class)
class CommentCrudServiceImplTest {

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private NewsCrudService newsCrudService;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentCrudServiceImpl commentCrudService;

    @Test
    void checkSaveSuccessfully() {
        final Long newsId = 1L;
        CommentToPostDto commentToPostDto = CommentToPostTestBuilder.aCommentToPost().buildDto();
        CommentDto commentDto = CommentTestBuilder.aComment().buildDto();
        Comment comment = CommentTestBuilder.aComment().withText(commentToPostDto.getText()).build();
        NewsDto newsDto = NewsTestBuilder.aNews().buildDto();

        Mockito.doReturn(commentDto).when(commentMapper)
                .toDto(comment);
        Mockito.doReturn(comment).when(commentMapper)
                .commentPostToEntity(commentToPostDto);
        Mockito.doReturn(newsDto).when(newsCrudService)
                .findById(newsId);
        Mockito.doReturn(comment).when(commentRepository)
                .save(comment);

        commentCrudService.save(newsId, commentToPostDto);

        assertThat(commentToPostDto.getNews()).isEqualTo(newsDto);
    }

    @ParameterizedTest
    @MethodSource("ru.clevertec.service.impl.CommentCrudServiceImplTest#getId")
    void checkDeleteSuccessfully(Long id) {
        commentCrudService.delete(id);

        Mockito.verify(commentRepository).deleteById(id);
    }

    @Nested
    class UpdateTest {

        @ParameterizedTest
        @MethodSource("ru.clevertec.service.impl.CommentCrudServiceImplTest#getText")
        void successfullyUpdate(String actualText, String expectedText) {
            CommentWithUpdateDto commentWithUpdateDto = CommentWithUpdateTestBuilder.aCommentWithUpdate().withText(actualText).buildDto();
            Comment comment = CommentTestBuilder.aComment().withText(expectedText).build();
            CommentDto expectedCommentDto = CommentTestBuilder.aComment().buildDto();

            Mockito.doReturn(Optional.of(comment)).when(commentRepository)
                    .findById(Long.parseLong(commentWithUpdateDto.getId()));
            Mockito.doReturn(expectedCommentDto).when(commentMapper).toDto(comment);

            CommentDto actualCommentDto = commentCrudService.update(commentWithUpdateDto);

            assertThat(comment.getText()).isEqualTo(commentWithUpdateDto.getText());
            assertThat(actualCommentDto).isEqualTo(expectedCommentDto);
        }

        @ParameterizedTest
        @MethodSource("ru.clevertec.service.impl.CommentCrudServiceImplTest#getId")
        void shouldThrowCommentNotFoundExceptionUpdate(Long id) {
            CommentWithUpdateDto commentWithUpdateDto = CommentWithUpdateTestBuilder.aCommentWithUpdate().withId(id.toString()).buildDto();
            String expected = "No comment with id = " + id + " found";

            Mockito.doReturn(Optional.empty()).when(commentRepository)
                    .findById(Long.parseLong(commentWithUpdateDto.getId()));

            CommentNotFoundException exception = assertThrows(CommentNotFoundException.class,
                    () -> commentCrudService.update(commentWithUpdateDto));
            String actual = exception.getMessage();
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class FindByIdTest {

        @ParameterizedTest
        @MethodSource("ru.clevertec.service.impl.CommentCrudServiceImplTest#getId")
        void successfullyFindById(Long id) {
            Comment comment = CommentTestBuilder.aComment().withId(id).build();
            CommentDto expected = CommentTestBuilder.aComment().withId(id).buildDto();

            Mockito.doReturn(Optional.of(comment)).when(commentRepository)
                    .findById(id);
            Mockito.doReturn(expected).when(commentMapper)
                    .toDto(comment);

            CommentDto actual = commentCrudService.findById(id);

            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @MethodSource("ru.clevertec.service.impl.CommentCrudServiceImplTest#getId")
        void shouldReturnFindById(Long id) {
            String expected = "No comment with id = " + id + " found";

            Mockito.doReturn(Optional.empty()).when(commentRepository)
                    .findById(id);

            CommentNotFoundException exception = assertThrows(CommentNotFoundException.class,
                    () -> commentCrudService.findById(id));
            String actual = exception.getMessage();
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Test
    void getNewsComments() {
        Long newsId = 1L;
        Comment comment1 = CommentTestBuilder.aComment().withId(1L).build();
        Comment comment2 = CommentTestBuilder.aComment().withId(2L).build();
        Comment comment3 = CommentTestBuilder.aComment().withId(3L).build();
        CommentDto commentDto1 = CommentTestBuilder.aComment().buildDto();
        CommentDto commentDto2 = CommentTestBuilder.aComment().buildDto();
        CommentDto commentDto3 = CommentTestBuilder.aComment().buildDto();

        List<Comment> commentList = List.of(comment1, comment2, comment3);
        List<CommentDto> commentDtoList = List.of(commentDto1, commentDto2, commentDto3);

        Page<Comment> commentPage = new PageImpl<>(commentList);
        Page<CommentDto> expected = new PageImpl<>(commentDtoList);

        Pageable request = PageRequest.of(0, 3);

        Mockito.doReturn(commentDto1).when(commentMapper)
                .toDto(comment1);
        Mockito.doReturn(commentDto2).when(commentMapper)
                .toDto(comment2);
        Mockito.doReturn(commentDto3).when(commentMapper)
                .toDto(comment3);
        Mockito.doReturn(commentPage).when(commentRepository)
                .findByNews_Id(newsId, request);

        Page<CommentDto> actual = commentCrudService.getNewsComments(newsId, request);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getByFilter() {
        Comment comment1 = CommentTestBuilder.aComment().withId(1L).build();
        Comment comment2 = CommentTestBuilder.aComment().withId(2L).build();
        Comment comment3 = CommentTestBuilder.aComment().withId(3L).build();
        CommentDto commentDto1 = CommentTestBuilder.aComment().buildDto();
        CommentDto commentDto2 = CommentTestBuilder.aComment().buildDto();
        CommentDto commentDto3 = CommentTestBuilder.aComment().buildDto();
        CommentFilter commentFilter = new CommentFilter();

        List<Comment> commentList = List.of(comment1, comment2, comment3);
        List<CommentDto> commentDtoList = List.of(commentDto1, commentDto2, commentDto3);

        Page<Comment> commentPage = new PageImpl<>(commentList);
        Page<CommentDto> expected = new PageImpl<>(commentDtoList);

        Pageable request = PageRequest.of(0, 3);

        Mockito.doReturn(commentDto1).when(commentMapper)
                .toDto(comment1);
        Mockito.doReturn(commentDto2).when(commentMapper)
                .toDto(comment2);
        Mockito.doReturn(commentDto3).when(commentMapper)
                .toDto(comment3);
        Mockito.doReturn(commentPage).when(commentRepository)
                .findAll((Specification<Comment>) Mockito.any(), Mockito.any());

        Page<CommentDto> actual = commentCrudService.getByFilter(commentFilter, request);

        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> getText() {
        return Stream.of(
                arguments("Kimmy", "Roger"),
                arguments("Integer", "Long"),
                arguments("Al'gol'", "Proxima"));
    }

    static Stream<Arguments> getId() {
        return Stream.of(
                arguments(1L),
                arguments(2000L),
                arguments(32455L));
    }
}
