package ru.practicum.ewm.ewmservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.ewmservice.dto.*;
import ru.practicum.ewm.ewmservice.model.EventState;
import ru.practicum.ewm.ewmservice.services.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @GetMapping("")
    public ResponseEntity<List<EventFullDto>> findAllEvents(@RequestParam(required = false) List<Long> users,
                                                            @RequestParam(required = false) List<EventState> states,
                                                            @RequestParam(required = false) List<Long> categories,
                                                            @RequestParam(required = false)
                                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                LocalDateTime rangeStart,
                                                            @RequestParam(required = false)
                                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                LocalDateTime rangeEnd,
                                                            @RequestParam(required = false) Integer from,
                                                            @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(eventService.findAllEvents(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@Valid @RequestBody EventUpdatetDto eventUpdatetDto,
                                                          @PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.updateEvent(eventUpdatetDto, null, eventId));
    }

}
