package ru.clevertec.logging.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "logging")
public class LoggingProperties {

    private boolean enabled;

}
