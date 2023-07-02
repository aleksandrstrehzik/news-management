package ru.clevertec.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.exceptionhandler.exceptions.validation.NewsValidationException;
import ru.clevertec.logging.annotations.AfterReturningLog;
import ru.clevertec.logging.annotations.AroundLog;
import ru.clevertec.logging.annotations.BeforeLog;
import ru.clevertec.service.dto.NewsDto;
import ru.clevertec.service.dto.NewsToPostDto;
import ru.clevertec.service.dto.NewsWithUpdateDto;
import ru.clevertec.service.dto.PageResponse;
import ru.clevertec.service.interfaces.NewsCrudService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
@Tag(name = "News CRUD Controller", description = "Methods for working with news")
public class NewsCrudController {

    private final NewsCrudService newsCrudService;


    /**
     * Возвращает обЪект типа NewsDto по заданному id.
     *
     * @param id Уникальный индефикатор объекта, Long id
     * @return Объект класса ResponseEntity содержащий статус ответа и объект класса NewsDto
     */
    @AfterReturningLog
    @GetMapping("/{id}")
    @Operation(summary = "Information about news by id without comments")
    public ResponseEntity<NewsDto> getById(@Parameter(description = "Unique news key") @PathVariable
                                                   Long id) {
        NewsDto newsDto = newsCrudService.findById(id);
        return ResponseEntity.ok(newsDto);
    }

    /**
     * Удаляет объект News из базы данных
     *
     * @param id Уникальный индефикатор объекта, Long id
     */
    @BeforeLog
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Deletes a news if the user has access")
    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('JOURNALIST') and @newsCrudServiceImpl.findById(#id).createBy.equals(authentication.name)")
    public void deleteById(@Parameter(description = "Unique news key") @PathVariable
                                   Long id) {
        newsCrudService.delete(id);
    }

    /**
     * Добавляет News в базу данных по имени аутенфицированного пользователя
     *
     * @param newsToPost Объект класса NewsToPostDto содержащий в себе комментарий
     * @return Объект класса ResponseEntity содержащий статус ответа и объект класса NewsDto
     */
    @AroundLog
    @PostMapping
    @Operation(summary = "Post a news if the user has access")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    public ResponseEntity<NewsDto> post(@RequestBody @Validated
                                                NewsToPostDto newsToPost,
                                        @Parameter(hidden = true)
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new NewsValidationException(bindingResult);
        }
        NewsDto savedNews = newsCrudService.save(newsToPost);
        return new ResponseEntity<>(savedNews, HttpStatus.CREATED);
    }

    /**
     * Изменяет title и/или text объекта News если у пользователя есть право
     *
     * @param newsWithUpdateDto Объект класса NewsWithUpdateDto содержащий в себе измененную новость
     * @return Объект класса ResponseEntity содержащий статус ответа и объект класса NewsDto
     */
    @AroundLog
    @PatchMapping
    @Operation(summary = "Update a news if the user has access")
    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('JOURNALIST') and @newsCrudServiceImpl.findById(#newsWithUpdateDto.id).createBy.equals(authentication.name)")
    public ResponseEntity<NewsDto> put(@RequestBody @Validated
                                               NewsWithUpdateDto newsWithUpdateDto,
                                       @Parameter(hidden = true)
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new NewsValidationException(bindingResult);
        }
        NewsDto updateNews = newsCrudService.update(newsWithUpdateDto);
        return new ResponseEntity<>(updateNews, HttpStatus.ACCEPTED);
    }

    /**
     * Возвращает все новости по условиям пагинации
     *
     * @param pageable Объект класса Pageable содержащий в условия пагинации
     * @return Объект класса ResponseEntity содержащий объект PageResponse в котором находятся новости и статус ответа
     */
    @AfterReturningLog
    @GetMapping
    @Operation(summary = "Give all news")
    public ResponseEntity<PageResponse<NewsDto>> findAll(@PageableDefault(size = 5, sort = "time") @Parameter(hidden = true)
                                                                 Pageable pageable) {
        Page<NewsDto> allNews = newsCrudService.findAll(pageable);
        return ResponseEntity.ok(PageResponse.of(allNews));
    }

}
