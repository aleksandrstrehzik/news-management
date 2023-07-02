package ru.clevertec.exceptionhandler.exceptions.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.exceptionhandler.exceptions.ErrorInfo;
import ru.clevertec.exceptionhandler.exceptions.entitynotfound.EntityNotFoundException;
import ru.clevertec.exceptionhandler.exceptions.validation.EntityValidationException;

@ControllerAdvice
public class EntityExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorInfo> handleEntityNotFoundException(HttpServletRequest request, EntityNotFoundException exception) {
        ErrorInfo errorResponse = ErrorInfo.builder()
                .exceptionMessage(exception.getMessage())
                .url(String.valueOf(request.getRequestURL()))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EntityValidationException.class})
    public ResponseEntity<ErrorInfo> handleEntityValidationException(HttpServletRequest request, EntityValidationException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        String allErrors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " : " + fieldError.getDefaultMessage())
                .reduce("", (a, b) -> a + ", " + b);
        ErrorInfo errorResponse = ErrorInfo.builder()
                .exceptionMessage(allErrors)
                .url(String.valueOf(request.getRequestURL()))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
