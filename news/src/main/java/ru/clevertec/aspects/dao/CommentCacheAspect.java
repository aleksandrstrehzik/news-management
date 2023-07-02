package ru.clevertec.aspects.dao;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import ru.clevertec.cache.CacheAlgorithm;
import ru.clevertec.cache.impl.CacheAlgorithmFactory;
import ru.clevertec.repository.entity.Comment;

import java.util.Optional;

@Aspect
@Component
public class CommentCacheAspect {

    private final CacheAlgorithm<Long, Comment> cache;

    public CommentCacheAspect(CacheAlgorithmFactory factory) {
        this.cache = (CacheAlgorithm<Long, Comment>) factory.getAlgorithm();
    }

    @Pointcut("bean(commentRepository)")
    public void isWithCommentRepository() {
    }

    @Around("isWithCommentRepository() && ru.clevertec.aspects.dao.CommonPointCuts.isWithFindByIdMethod() && args(id)")
    public Optional<Comment> findById(ProceedingJoinPoint point, Long id) throws Throwable {
        Comment commentFromCache = cache.get(id);
        if (commentFromCache == null) {
            Optional<Comment> commentFromRepository = (Optional<Comment>) point.proceed();
            if (commentFromRepository.isPresent()) {
                Comment comment = commentFromRepository.get();
                cache.post(comment.getId(), comment);
            }
            return commentFromRepository;
        } else return Optional.of(commentFromCache);
    }

    @AfterReturning(value = "isWithCommentRepository() && ru.clevertec.aspects.dao.CommonPointCuts.isWithSaveMethod()", returning = "comment")
    public void save(Comment comment) {
        Long id = comment.getId();
        if (cache.get(id) == null) {
            cache.post(id, comment);
        } else cache.put(id, comment);
    }

    @After("isWithCommentRepository() && ru.clevertec.aspects.dao.CommonPointCuts.isWithDeleteMethod() && args(id)")
    public void delete(Long id) {
        cache.delete(id);
    }
}
