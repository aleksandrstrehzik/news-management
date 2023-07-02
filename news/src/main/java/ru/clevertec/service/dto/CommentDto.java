package ru.clevertec.service.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Instant time;
    private String text;
    private String username;
    private NewsDto news;

}
