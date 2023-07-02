package ru.clevertec.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.exceptionhandler.exceptions.validation.CommentValidationException;
import ru.clevertec.logging.annotations.AfterReturningLog;
import ru.clevertec.logging.annotations.AroundLog;
import ru.clevertec.logging.annotations.BeforeLog;
import ru.clevertec.service.dto.CommentDto;
import ru.clevertec.service.dto.CommentToPostDto;
import ru.clevertec.service.dto.CommentWithUpdateDto;
import ru.clevertec.service.interfaces.CommentCrudService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
@Tag(name = "Comment CRUD Controller", description = "Methods for working with comments")
public class CommentCrudController {

    private final CommentCrudService commentCrudService;

    /**
     * Возвращает обЪект типа CommentDto по заданному id.
     *
     * @param id Уникальный индефикатор объекта, Long id
     * @return Объект класса ResponseEntity содержащий статус ответа и объект класса CommentDto
     */
    @AfterReturningLog
    @GetMapping("/{id}")
    @Operation(summary = "Information about comment by id")
    public ResponseEntity<CommentDto> getById(@Parameter(description = "Unique comment key") @PathVariable
                                                      Long id) {
        CommentDto commentDto = commentCrudService.findById(id);
        return ResponseEntity.ok(commentDto);
    }

    /**
     * Удаляет объект Comment из базы данных
     *
     * @param id Уникальный индефикатор объекта, Long id
     */
    @BeforeLog
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Deletes a comment if the user has access")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUBSCRIBER') and @commentCrudServiceImpl.findById(#id).username.equals(authentication.name)")
    public void deleteById(@Parameter(description = "Unique comment key") @PathVariable("id")
                                   Long id) {
        commentCrudService.delete(id);
    }

    /**
     * Добавляет Comment в базу данных по указанному newsId и имени аутенфицированного пользователя
     *
     * @param newsId Уникальный индефикатор объекта типа News, Long newsId
     * @param commentToPost Объект класса CommentToPostDto содержащий в себе комментарий
     * @return Объект класса ResponseEntity содержащий статус ответа и объект класса CommentDto
     */
    @AroundLog
    @PostMapping("/{newsId}")
    @Operation(summary = "Post a comment if the user has access")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUBSCRIBER')")
    public ResponseEntity<CommentDto> post(@Parameter(description = "The unique key of the news to which the comment should be attached.") @PathVariable
                                                   Long newsId,
                                           @RequestBody @Validated
                                                   CommentToPostDto commentToPost,
                                           @Parameter(hidden = true)
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CommentValidationException(bindingResult);
        }
        CommentDto savedCommentDto = commentCrudService.save(newsId, commentToPost);
        return new ResponseEntity<>(savedCommentDto, HttpStatus.CREATED);
    }

    /**
     * Изменяет комментарий если у пользователя есть право
     *
     * @param commentWithUpdate Объект класса CommentWithUpdateDto содержащий в изменненый себе комментарий
     * @return Объект класса ResponseEntity содержащий статус ответа и объект класса CommentDto
     */
    @BeforeLog
    @PatchMapping
    @Operation(summary = "Update a comment if the user has access")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUBSCRIBER') and @commentCrudServiceImpl.findById(#commentWithUpdate.id).username.equals(authentication.name)")
    public ResponseEntity<CommentDto> put(@RequestBody @Validated
                                                  CommentWithUpdateDto commentWithUpdate,
                                          @Parameter(hidden = true)
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CommentValidationException(bindingResult);
        }
        CommentDto updateComment = commentCrudService.update(commentWithUpdate);
        return new ResponseEntity<>(updateComment, HttpStatus.ACCEPTED);
    }
}
