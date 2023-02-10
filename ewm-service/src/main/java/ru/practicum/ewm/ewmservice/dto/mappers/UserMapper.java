package ru.practicum.ewm.ewmservice.dto.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.ewmservice.dto.UserDto;
import ru.practicum.ewm.ewmservice.dto.UserShortDto;
import ru.practicum.ewm.ewmservice.model.User;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    UserDto userToUserDto(User user);

    User userShortDtoToUser(UserShortDto userShortDto);
}
