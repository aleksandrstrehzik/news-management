package ru.clevertec.exceptionhandler.exceptions.entitynotfound;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
