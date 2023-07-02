package ru.clevertec.exceptionhandler.exceptions.entitynotfound;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EntityNotFoundException extends RuntimeException {

    private String message;
}
