package ru.practicum.ewm.ewmservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.ewmservice.dto.RequestDto;
import ru.practicum.ewm.ewmservice.services.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestPrivateController {
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<RequestDto>> findAllUserRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(requestService.findAllUserRequests(userId));
    }

    @PostMapping
    public ResponseEntity<RequestDto> createRequest(@PathVariable Long userId,
                                                    @RequestParam Long eventId) {
        return new ResponseEntity<>(requestService.createRequest(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelRequest(@PathVariable Long userId,
                                                    @PathVariable Long requestId) {
        return ResponseEntity.ok(requestService.cancelRequest(userId, requestId));
    }
}
