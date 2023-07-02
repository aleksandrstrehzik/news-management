package ru.clevertec.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.service.dto.NewsDto;
import ru.clevertec.service.dto.NewsToPostDto;
import ru.clevertec.service.dto.NewsWithUpdateDto;
import ru.clevertec.service.specifications.NewsFilter;

public interface NewsCrudService extends GetByFilter<NewsDto, NewsFilter>{

    /**
     * Добавляет News в базу данных по имени аутенфицированного пользователя
     *
     * @param newsToPost Объект класса NewsToPostDto содержащий в себе комментарий
     * @return Объект класса NewsDto
     */
    NewsDto save(NewsToPostDto newsToPost);

    /**
     * Удаляет объект News из базы данных
     *
     * @param id Уникальный индефикатор объекта, Long id
     */
    void delete(Long id);

    /**
     * Изменяет title и/или text объекта
     *
     * @param newsWithUpdateDto Объект класса NewsWithUpdateDto содержащий в себе измененную новость
     * @return Объект класса NewsDto
     */
    NewsDto update(NewsWithUpdateDto newsWithUpdateDto);

    /**
     * Возвращает обЪект типа NewsDto по заданному id.
     *
     * @param id Уникальный индефикатор объекта, Long id
     * @return Объект класса NewsDto
     */
    NewsDto findById(Long id);

    /**
     * Возвращает все новости по условиям пагинации
     *
     * @param pageable Объект класса Pageable содержащий в условия пагинации
     * @return Объект класса Page в котором находятся NewsDto и статус ответа
     */
    Page<NewsDto> findAll(Pageable pageable);
}
