package ru.practicum.ewm.ewmservice.services;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.ewmservice.dto.RequestDto;

import java.util.List;

@Service
public interface RequestService {
    List<RequestDto> findAllUserRequests(Long userId);

    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);
}
