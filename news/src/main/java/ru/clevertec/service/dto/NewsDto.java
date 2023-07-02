package ru.clevertec.service.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {

    private Long id;
    private Instant time;
    private String title;
    private String text;
    private String createBy;
    private List<CommentDto> comments;
}
