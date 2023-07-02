package ru.clevertec.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.repository.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Возвращает все комментарии определенной новости по условиям пагинации
     *
     * @param pageable Объект класса Pageable содержащий в условия пагинации
     * @param newsId Уникальный индефикатор новости, Long id
     * @return Объект класса Page в котором находятся Comment и статус ответа
     */
    @Query("select c from Comment c where c.news.id = :newsId")
    Page<Comment> findByNews_Id(Long newsId, Pageable pageable);

    /**
     * Возвращает все комментарии по условиям пагинации и фильтру
     *
     * @param pageable Объект класса Pageable содержащий в условия пагинации
     * @param commentSpecification Объект класса Specification содержащий условия поиска
     * @return Объект класса Page в котором находятся Comment и статус ответа
     */
    Page<Comment> findAll(Specification<Comment> commentSpecification, Pageable pageable);

}
