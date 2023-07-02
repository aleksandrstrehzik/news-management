package ru.clevertec.exceptionhandler.exceptions.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
@AllArgsConstructor
public class EntityValidationException extends RuntimeException{

    private BindingResult bindingResult;
}
