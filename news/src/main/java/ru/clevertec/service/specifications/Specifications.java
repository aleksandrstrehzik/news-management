package ru.clevertec.service.specifications;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.clevertec.repository.entity.Comment;
import ru.clevertec.repository.entity.News;

import java.util.ArrayList;
import java.util.List;

@Component
public class Specifications {

    public static Specification<News> getNewsSpecification(NewsFilter newsFilter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (newsFilter.getText() != null && !newsFilter.getText().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("text"),
                        "%" + newsFilter.getText() + "%"));
            }
            if (newsFilter.getTitle() != null && !newsFilter.getTitle().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"),
                        "%" + newsFilter.getTitle() + "%"));
            }
            if (newsFilter.getTime() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("time"), newsFilter.getTime()));
            }
            if (newsFilter.getCreateBy() != null && !newsFilter.getCreateBy().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("createBy"),
                        "%" + newsFilter.getCreateBy() + "%"));
            }
            query.orderBy(criteriaBuilder.desc(root.get("time")));
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static Specification<Comment> getCommentSpecification(CommentFilter commentFilter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (commentFilter.getText() != null && !commentFilter.getText().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("text"),
                        "%" + commentFilter.getText() + "%"));
            }
            if (commentFilter.getUsername() != null && !commentFilter.getUsername().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("username"),
                        "%" + commentFilter.getUsername() + "%"));
            }
            if (commentFilter.getTime() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("time"), commentFilter.getTime()));
            }
            query.orderBy(criteriaBuilder.desc(root.get("time")));
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
