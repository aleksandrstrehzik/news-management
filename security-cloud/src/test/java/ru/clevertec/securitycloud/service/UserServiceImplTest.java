package ru.clevertec.securitycloud.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.exceptionhandler.exceptions.entitynotfound.UserNotFoundException;
import ru.clevertec.securitycloud.repository.UserRepository;
import ru.clevertec.securitycloud.repository.entity.User;
import ru.clevertec.securitycloud.repository.entity.roles.Role;
import ru.clevertec.securitycloud.service.dto.UserDto;
import ru.clevertec.securitycloud.service.mapper.UserMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserSuccessfully() {
        User user = User.builder()
                .username("Vasia")
                .password("1")
                .role(Role.ADMIN)
                .build();
        UserDto expected = UserDto.builder()
                .username("Vasia")
                .password("1")
                .role(Role.ADMIN)
                .build();

        Mockito.doReturn(Optional.of(user))
                .when(userRepository).findByUsername("Vasia");
        Mockito.doReturn(expected)
                .when(userMapper).toUserWithOutId(user);

        UserDto actual = userService.getUser("Vasia");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getUserNotFoundException() {
        Mockito.doReturn(Optional.empty())
                .when(userRepository).findByUsername("Vasia");
        String expected = "No User with username = Vasia found";

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUser("Vasia"));

        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
    }
}
