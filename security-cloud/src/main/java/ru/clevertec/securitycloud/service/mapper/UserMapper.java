package ru.clevertec.securitycloud.service.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.securitycloud.repository.entity.User;
import ru.clevertec.securitycloud.service.dto.UserDto;

@Mapper
public interface UserMapper {

    UserDto toUserWithOutId(User user);
}
