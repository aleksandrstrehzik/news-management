package ru.clevertec.controller.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.service.dto.UserDto;

@FeignClient(value = "${feignclient.value}", url = "${feignclient.url}")
public interface UserFeignController {

    /**
     * Метод для обращения к стронему сервису для получения данных о пользователе
     *
     * @param name Объект класса String содержащий имя пользователя
     * @return Объект класса ResponseEntity объект класса UserDto и статус ответа
     */
    @GetMapping("/{name}")
    ResponseEntity<UserDto> getUserByName(@PathVariable String name);
}
