package ru.clevertec.service.specifications;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsFilter {

    @Size(max = 100, message = "Search text cannot be more than 100 characters")
    private String title;

    @Size(max = 400, message = "Search text cannot be more than 400 characters")
    private String text;

    @Size(max = 50, message = "Search text cannot be more than 50 characters")
    private String createBy;

    @Past
    private Instant time;

}
