package ru.clevertec.exceptionhandler.exceptions;

import lombok.Builder;

@Builder
public class ErrorInfo {

    public final String url;
    public final String exceptionMessage;
}
