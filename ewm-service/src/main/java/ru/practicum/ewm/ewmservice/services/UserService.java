package ru.practicum.ewm.ewmservice.services;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.ewmservice.dto.UserDto;
import ru.practicum.ewm.ewmservice.dto.UserShortDto;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> findAllUsers(List<Long> ids, Integer from, Integer size);

    UserDto createUser(UserShortDto userShortDto);

    void deleteUser(Long id);
}
