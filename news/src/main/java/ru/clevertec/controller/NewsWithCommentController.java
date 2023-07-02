package ru.clevertec.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.logging.annotations.AfterReturningLog;
import ru.clevertec.service.dto.CommentDto;
import ru.clevertec.service.dto.NewsDto;
import ru.clevertec.service.dto.NewsWithCommentsResponse;
import ru.clevertec.service.interfaces.CommentCrudService;
import ru.clevertec.service.interfaces.NewsCrudService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/newsWithComments")
@Tag(name = "News With Comment Controller", description = "Method return news with comments")
public class NewsWithCommentController {

    private final NewsCrudService newsCrudService;
    private final CommentCrudService commentCrudService;

    /**
     * Возвращает обЪект типа NewsDto по заданному id с относящимися к ней комментариями.
     *
     * @param id Уникальный индефикатор объекта, Long id
     * @param pageable Объект класса Pageable содержащий в условия пагинации
     * @return Объект класса ResponseEntity содержащий статус ответа и объект класса NewsWithCommentsResponse
     */
    @AfterReturningLog
    @GetMapping("/{id}")
    @Operation(summary = "Information about news by id with comments")
    public ResponseEntity<NewsWithCommentsResponse> getByIdNewsWithComments(@PathVariable @Parameter(description = "Unique news key")
                                                                    Long id,
                                                            @PageableDefault(size = 3, sort = "time")
                                                                    Pageable pageable) {
        Page<CommentDto> newsComments = commentCrudService.getNewsComments(id, pageable);
        NewsDto news = newsCrudService.findById(id);
        return ResponseEntity.ok(NewsWithCommentsResponse.of(newsComments, news));
    }
}
