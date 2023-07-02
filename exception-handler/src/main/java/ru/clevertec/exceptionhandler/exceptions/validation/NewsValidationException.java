package ru.clevertec.exceptionhandler.exceptions.validation;

import org.springframework.validation.BindingResult;

public class NewsValidationException extends EntityValidationException {

    public NewsValidationException(BindingResult bindingResult) {
        super(bindingResult);
    }
}
