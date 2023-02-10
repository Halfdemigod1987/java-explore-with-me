package ru.practicum.ewm.ewmservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.ewmservice.dto.*;
import ru.practicum.ewm.ewmservice.services.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;

    @GetMapping("")
    public ResponseEntity<List<EventShortDto>> findAllUserEvents(@PathVariable Long userId,
                                                                 @RequestParam(required = false) Integer from,
                                                                 @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(eventService.findAllUserEvents(userId, from, size));
    }

    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(@Valid @RequestBody EventNewDto eventNewDto,
                                                    @PathVariable Long userId) {
        return new ResponseEntity<>(eventService.createEvent(eventNewDto, userId), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> findUserEventById(@PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.findUserEventById(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@Valid @RequestBody EventUpdatetDto eventUpdatetDto,
                                                    @PathVariable Long userId,
                                                    @PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.updateEvent(eventUpdatetDto, userId, eventId));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> findAllEventRequests(@PathVariable Long userId,
                                                                 @PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.findAllEventRequests(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<RequestUpdateResultDto> updateEventRequest(
            @RequestBody RequestUpdateDto requestUpdateDto,
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.updateEventRequests(requestUpdateDto, userId, eventId));
    }

}
