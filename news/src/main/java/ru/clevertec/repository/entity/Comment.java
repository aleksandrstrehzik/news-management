package ru.clevertec.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Setter
@Getter
@Builder
@ToString(exclude = "news")
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "final_task", name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private Instant time;

    @CreatedBy
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    private String text;

}
