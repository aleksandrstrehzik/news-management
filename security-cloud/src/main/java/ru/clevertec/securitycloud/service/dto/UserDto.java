package ru.clevertec.securitycloud.service.dto;

import lombok.*;
import ru.clevertec.securitycloud.repository.entity.roles.Role;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Role role;
    private String username;
    private String password;

}
