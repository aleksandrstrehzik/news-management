package ru.clevertec.securitycloud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.exceptionhandler.exceptions.entitynotfound.UserNotFoundException;
import ru.clevertec.securitycloud.repository.UserRepository;
import ru.clevertec.securitycloud.repository.entity.User;
import ru.clevertec.securitycloud.service.dto.UserDto;
import ru.clevertec.securitycloud.service.mapper.UserMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Возвращает обЪект типа UserDto по заданному имени.
     *
     * @param username Уникальный имя объекта, String name
     * @return Объект класса UserDto
     */
    public UserDto getUser(String username) {
        return userMapper.toUserWithOutId(getByUsername(username));
    }

    private User getByUsername(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            throw new UserNotFoundException("No User with username = " + username + " found");
        } else return byUsername.get();
    }
}
