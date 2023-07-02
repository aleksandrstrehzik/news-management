package ru.clevertec.securitycloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.securitycloud.service.UserServiceImpl;
import ru.clevertec.securitycloud.service.dto.UserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserServiceImpl userService;

    /**
     * Возвращает обЪект типа UserDto по заданному имени.
     *
     * @param name Уникальный имя объекта, String name
     * @return Объект класса ResponseEntity содержащий статус ответа и объект класса UserDto
     */
    @GetMapping("/{name}")
    public ResponseEntity<UserDto> getUserByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.getUser(name));
    }
}
