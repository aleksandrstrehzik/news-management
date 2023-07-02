package ru.clevertec.securitycloud.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.clevertec.securitycloud.repository.entity.roles.Role;

@Entity
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(schema = "final_task", name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String username;
    private String password;

}
