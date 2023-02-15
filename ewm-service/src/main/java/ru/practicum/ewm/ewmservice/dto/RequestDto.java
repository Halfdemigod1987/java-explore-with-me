package ru.practicum.ewm.ewmservice.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.ewmservice.model.RequestStatus;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class RequestDto {
    LocalDateTime created;
    Integer event;
    Integer id;
    Integer requester;
    RequestStatus status;
}
