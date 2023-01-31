package ru.practicum.ewm.statsdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import java.time.LocalDateTime;

@Value
@AllArgsConstructor
@Builder
@Jacksonized
public class EndpointHitReturnDto {
    Long id;
    String app;
    String uri;
    LocalDateTime timestamp;
}
