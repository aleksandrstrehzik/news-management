package ru.clevertec.exceptionhandler.exceptions.validation;

import org.springframework.validation.BindingResult;

public class CommentValidationException extends EntityValidationException {

    public CommentValidationException(BindingResult bindingResult) {
        super(bindingResult);
    }
}
