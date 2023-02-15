package ru.practicum.ewm.ewmservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.ewmservice.dao.UserRepository;
import ru.practicum.ewm.ewmservice.dto.UserDto;
import ru.practicum.ewm.ewmservice.dto.UserShortDto;
import ru.practicum.ewm.ewmservice.dto.mappers.UserMapper;
import ru.practicum.ewm.ewmservice.exceptions.UserNotFoundException;
import ru.practicum.ewm.ewmservice.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAllUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable;
        if (size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageable = PageRequest.of(from / size, size);
        }
        if (ids != null) {
            return userRepository.findByIdIn(ids, pageable)
                    .stream()
                    .map(userMapper::userToUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(pageable)
                    .stream()
                    .map(userMapper::userToUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public UserDto createUser(UserShortDto userShortDto) {
        return userMapper.userToUserDto(
                userRepository.save(userMapper.userShortDtoToUser(userShortDto)));
    }

    @Override
    public void deleteUser(Long id) {
        findUserById(id);
        userRepository.deleteById(id);
    }

    private void findUserById(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                    String.format("User with id=%d was not found", id)));
    }
}
