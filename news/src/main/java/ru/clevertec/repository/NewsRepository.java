package ru.clevertec.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.repository.entity.News;

public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Возвращает все новости по условиям пагинации и фильтру
     *
     * @param pageable Объект класса Pageable содержащий в условия пагинации
     * @param specification Объект класса Specification содержащий условия поиска
     * @return Объект класса Page в котором находятся NewsDto и статус ответа
     */
    Page<News> findAll(Specification<News> specification, Pageable pageable);

}
