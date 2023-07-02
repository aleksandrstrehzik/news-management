package ru.clevertec.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.controller.feign.UserFeignController;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserDetailsService {

    private final UserFeignController userFeignController;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userFeignController.getUserByName(username).getBody();
    }
}
