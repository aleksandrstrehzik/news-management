package ru.clevertec.exceptionhandler.exceptions.entitynotfound;

public class CommentNotFoundException extends EntityNotFoundException {

    public CommentNotFoundException(String message) {
        super(message);
    }
}
