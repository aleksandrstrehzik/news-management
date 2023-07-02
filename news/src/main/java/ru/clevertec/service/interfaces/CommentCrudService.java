package ru.clevertec.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.service.dto.CommentDto;
import ru.clevertec.service.dto.CommentToPostDto;
import ru.clevertec.service.dto.CommentWithUpdateDto;
import ru.clevertec.service.specifications.CommentFilter;

public interface CommentCrudService extends GetByFilter<CommentDto, CommentFilter>{

    /**
     * Добавляет Comment в базу данных по указанному newsId и имени аутенфицированного пользователя
     *
     * @param newsId Уникальный индефикатор объекта типа News, Long newsId
     * @param commentToPost Объект класса CommentToPostDto содержащий в себе комментарий
     * @return Объект класса CommentDto
     */
    CommentDto save(Long newsId, CommentToPostDto commentToPost);

    /**
     * Удаляет объект Comment из базы данных
     *
     * @param id Уникальный индефикатор объекта, Long id
     */
    void delete(Long id);

    /**
     * Изменяет комментарий если у пользователя есть право
     *
     * @param commentWithUpdateDto Объект класса CommentWithUpdateDto содержащий в изменненый себе комментарий
     * @return Объект класса CommentDto
     */
    CommentDto update(CommentWithUpdateDto commentWithUpdateDto);

    /**
     * Возвращает обЪект типа CommentDto по заданному id.
     *
     * @param id Уникальный индефикатор объекта, Long id
     * @return Объект класса CommentDto
     */
    CommentDto findById(Long id);

    /**
     * Возвращает обЪект типа Page по заданному id новости с относящимися к ней комментариями.
     *
     * @param newsId Уникальный индефикатор новости, Long id
     * @param pageable Объект класса Pageable содержащий в условия пагинации
     * @return Объект класса Page содержащий комментарии новости
     */
    Page<CommentDto> getNewsComments(Long newsId, Pageable pageable);
}
