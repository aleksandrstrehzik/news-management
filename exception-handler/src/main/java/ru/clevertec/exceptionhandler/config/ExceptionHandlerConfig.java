package ru.clevertec.exceptionhandler.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.exceptionhandler.exceptions.handler.EntityExceptionHandler;

@Configuration
@EnableConfigurationProperties(ExceptionProperties.class)
@ConditionalOnProperty(prefix = "exception.handling", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ExceptionHandlerConfig {

    @Bean
    public EntityExceptionHandler getExceptionHandler() {
        return new EntityExceptionHandler();
    }
}
