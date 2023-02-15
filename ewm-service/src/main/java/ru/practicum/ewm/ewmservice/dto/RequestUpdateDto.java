package ru.practicum.ewm.ewmservice.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.ewmservice.model.RequestStatus;

import java.util.List;

@Value
@Builder
@Jacksonized
public class RequestUpdateDto {
    List<Long> requestIds;
    RequestStatus status;
}
