package ru.clevertec.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.exceptionhandler.exceptions.entitynotfound.CommentNotFoundException;
import ru.clevertec.repository.CommentRepository;
import ru.clevertec.repository.entity.Comment;
import ru.clevertec.service.dto.CommentDto;
import ru.clevertec.service.dto.CommentToPostDto;
import ru.clevertec.service.dto.CommentWithUpdateDto;
import ru.clevertec.service.interfaces.CommentCrudService;
import ru.clevertec.service.interfaces.NewsCrudService;
import ru.clevertec.service.mappers.CommentMapper;
import ru.clevertec.service.specifications.CommentFilter;
import ru.clevertec.service.specifications.Specifications;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCrudServiceImpl implements CommentCrudService {

    private final CommentMapper commentMapper;
    private final NewsCrudService newsCrudService;
    private final CommentRepository commentRepository;

    @Override
    public CommentDto save(Long newsId, CommentToPostDto commentDto) {
        commentDto.setNews(newsCrudService.findById(newsId));
        Comment comment = commentMapper.commentPostToEntity(commentDto);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public CommentDto update(CommentWithUpdateDto commentWithUpdateDto) {
        Comment comment = getByIdOrThrowCommentNotFound(Long.parseLong(commentWithUpdateDto.getId()));
        comment.setText(commentWithUpdateDto.getText());
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto findById(Long id) {
        return commentMapper.toDto(getByIdOrThrowCommentNotFound(id));
    }

    @Override
    public Page<CommentDto> getByFilter(CommentFilter commentFilter, Pageable pageable) {
        Specification<Comment> commentSpecification = Specifications.getCommentSpecification(commentFilter);
        return commentRepository.findAll(commentSpecification, pageable).map(commentMapper::toDto);
    }

    @Override
    public Page<CommentDto> getNewsComments(Long newsId, Pageable pageable) {
        return commentRepository.findByNews_Id(newsId, pageable).map(commentMapper::toDto);
    }

    private Comment getByIdOrThrowCommentNotFound(Long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isEmpty()) {
            throw new CommentNotFoundException("No comment with id = " + id + " found");
        } else return commentOptional.get();
    }
}
