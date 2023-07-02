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
public class CommentFilter {

    @Size(max = 100, message = "Search text cannot be more than 100 characters")
    private String text;

    @Size(max = 50, message = "User name cannot larger than 50 characters")
    private String username;

    @Past
    private Instant time;
}
