package ru.clevertec.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.logging.annotations.BeforeLog;
import ru.clevertec.service.dto.CommentDto;
import ru.clevertec.service.dto.NewsDto;
import ru.clevertec.service.dto.PageResponse;
import ru.clevertec.service.interfaces.CommentCrudService;
import ru.clevertec.service.interfaces.NewsCrudService;
import ru.clevertec.service.specifications.CommentFilter;
import ru.clevertec.service.specifications.NewsFilter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/getByFilter")
@Tag(name = "Filter Controller", description = "Return news and comments according filter")
public class GetByFilterController {

    private final NewsCrudService newsCrudService;
    private final CommentCrudService commentCrudService;

    /**
     * Возвращает объект класса ResponseEntity содержащий объект PageResponse в котором находятся новости подходящие
     * по условиям
     *
     * @param newsFilter Объект класса NewsFilter содержащий в условия для поиска
     * @param pageable Объект класса Pageable содержащий в условия пагинации
     * @return Объект класса ResponseEntity содержащий объект PageResponse в котором находятся новости и статус ответа
     */
    @BeforeLog
    @GetMapping("/news")
    @Operation(summary = "Return news according filter")
    public ResponseEntity<PageResponse<NewsDto>> getAllNewsByFilter(@RequestBody @Validated
                                                                            NewsFilter newsFilter,
                                                                    @PageableDefault(size = 3, sort = "time")
                                                                            Pageable pageable) {
        Page<NewsDto> newsByFilter = newsCrudService.getByFilter(newsFilter, pageable);
        return ResponseEntity.ok(PageResponse.of(newsByFilter));
    }

    /**
     * Возвращает объект класса ResponseEntity содержащий объект PageResponse в котором находятся комментарии подходящие
     * по условиям
     *
     * @param commentFilter Объект класса CommentFilter содержащий в условия для поиска
     * @param pageable Объект класса Pageable содержащий в условия пагинации
     * @return Объект класса ResponseEntity содержащий объект PageResponse в котором находятся комментарии и статус ответа
     */
    @BeforeLog
    @GetMapping("/comment")
    @Operation(summary = "Return comment according filter")
    public ResponseEntity<PageResponse<CommentDto>> getAllCommentsByFilter(@RequestBody @Validated
                                                                                   CommentFilter commentFilter,
                                                                           @PageableDefault(size = 3, sort = "time")
                                                                                   Pageable pageable) {
        Page<CommentDto> newsByFilter = commentCrudService.getByFilter(commentFilter, pageable);
        return ResponseEntity.ok(PageResponse.of(newsByFilter));
    }
}
