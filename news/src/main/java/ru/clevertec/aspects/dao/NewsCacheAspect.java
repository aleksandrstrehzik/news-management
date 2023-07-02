package ru.clevertec.aspects.dao;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import ru.clevertec.cache.CacheAlgorithm;
import ru.clevertec.cache.impl.CacheAlgorithmFactory;
import ru.clevertec.repository.entity.News;

import java.util.Optional;

@Aspect
@Component
public class NewsCacheAspect {

    private final CacheAlgorithm<Long, News> cache;

    public NewsCacheAspect(CacheAlgorithmFactory factory) {
        this.cache = (CacheAlgorithm<Long, News>) factory.getAlgorithm();
    }

    @Pointcut("bean(newsRepository)")
    public void isWithNewsRepository() {
    }

    @Around("isWithNewsRepository() && ru.clevertec.aspects.dao.CommonPointCuts.isWithFindByIdMethod() && args(id)")
    public Optional<News> findById(ProceedingJoinPoint point, Long id) throws Throwable {
        News newsFromCache = cache.get(id);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(cache);
        if (newsFromCache == null) {
            Optional<News> newsFromRepository = (Optional<News>) point.proceed();
            if (newsFromRepository.isPresent()) {
                News news = newsFromRepository.get();
                cache.post(news.getId(), news);
            }
            return newsFromRepository;
        } else return Optional.of(newsFromCache);
    }

    @AfterReturning(value = "isWithNewsRepository() && ru.clevertec.aspects.dao.CommonPointCuts.isWithSaveMethod()", returning = "news")
    public void save(News news) {
        Long id = news.getId();
        System.out.println(cache);
        if (cache.get(id) == null) {
            cache.post(id, news);
        } else cache.put(id, news);
    }

    /*@Around("isWithNewsRepository() && ru.clevertec.aspects.dao.CommonPointCuts.isWithSaveMethod()")
    public News save(ProceedingJoinPoint joinPoint) {
        News news;
        try {
            news = (News) joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new NewsNotFoundException("this is not uniq");
        }
        Long id = news.getId();
        System.out.println(cache);
        if (cache.get(id) == null) {
            cache.post(id, news);
        } else cache.put(id, news);
        return news;
    }*/

    @After("isWithNewsRepository() && ru.clevertec.aspects.dao.CommonPointCuts.isWithDeleteMethod() && args(id)")
    public void delete(Long id) {
        cache.delete(id);
    }
}
