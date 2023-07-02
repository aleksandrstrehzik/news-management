package ru.clevertec.exceptionhandler.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "exception.handling")
public class ExceptionProperties {

    private boolean enabled;

}
