package ru.clevertec.service.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.clevertec.repository.entity.roles.Role;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements UserDetails {

    private Role role;
    private String username;
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
