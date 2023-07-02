package ru.clevertec.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.exceptionhandler.exceptions.entitynotfound.NewsNotFoundException;
import ru.clevertec.repository.NewsRepository;
import ru.clevertec.repository.entity.News;
import ru.clevertec.service.dto.NewsDto;
import ru.clevertec.service.dto.NewsToPostDto;
import ru.clevertec.service.dto.NewsWithUpdateDto;
import ru.clevertec.service.interfaces.NewsCrudService;
import ru.clevertec.service.mappers.NewsMapper;
import ru.clevertec.service.specifications.NewsFilter;
import ru.clevertec.service.specifications.Specifications;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsCrudServiceImpl implements NewsCrudService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Override
    public NewsDto save(NewsToPostDto newsDto) {
        News news = newsMapper.postNewsToEntity(newsDto);
        return newsMapper.toDto(newsRepository.save(news));
    }

    @Override
    public void delete(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public NewsDto update(NewsWithUpdateDto newsWithUpdateDto) {
        News news = getByIdOrThrowNotFound(Long.parseLong(newsWithUpdateDto.getId()));
        news.setText(newsWithUpdateDto.getText());
        news.setTitle(newsWithUpdateDto.getTitle());
        News savedNews = newsRepository.save(news);
        return newsMapper.toDto(savedNews);
    }

    @Override
    @Transactional(readOnly = true)
    public NewsDto findById(Long id) {
        return newsMapper.toDto(getByIdOrThrowNotFound(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NewsDto> findAll(Pageable pageable) {
        return newsRepository.findAll(pageable).map(newsMapper::toDtoWithOutIdAndComments);
    }

    @Override
    public Page<NewsDto> getByFilter(NewsFilter newsFilter, Pageable pageable) {
        Specification<News> newsSpecification = Specifications.getNewsSpecification(newsFilter);
        return newsRepository.findAll(newsSpecification, pageable).map(newsMapper::toDtoWithOutIdAndComments);
    }

    private News getByIdOrThrowNotFound(Long id) {
        Optional<News> newsOptional = newsRepository.findById(id);
        if (newsOptional.isEmpty()) {
            throw new NewsNotFoundException("No news with id = " + id + " found");
        } else return newsOptional.get();
    }
}
