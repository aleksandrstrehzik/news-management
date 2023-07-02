package ru.clevertec.logging.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.logging.aspects.CommonPointcuts;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnProperty(prefix = "logging", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LoggingConfig {

    @Bean
    public CommonPointcuts getCommonPointcuts() {
        return new CommonPointcuts();
    }
}
