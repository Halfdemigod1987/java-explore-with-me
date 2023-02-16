package ru.practicum.ewm.ewmservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.ewmservice.dto.EventFullDto;
import ru.practicum.ewm.ewmservice.services.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {
    private final EventService eventService;

    @GetMapping("")
    public ResponseEntity<List<EventFullDto>> findAllEvents(@RequestParam(required = false) String text,
                                                            @RequestParam(required = false) List<Long> categories,
                                                            @RequestParam(required = false) Boolean paid,
                                                            @RequestParam(required = false)
                                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                LocalDateTime rangeStart,
                                                            @RequestParam(required = false)
                                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                LocalDateTime rangeEnd,
                                                            @RequestParam(required = false) Boolean onlyAvailable,
                                                            @RequestParam(required = false) String sort,
                                                            @RequestParam(required = false) Integer from,
                                                            @RequestParam(required = false) Integer size,
                                                            HttpServletRequest request) {
        return ResponseEntity.ok(eventService.findAllPublicEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, request.getRequestURI(), request.getRemoteAddr()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> findEventById(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(eventService.findPublicEventById(id, request.getRequestURI(), request.getRemoteAddr()));
    }
}
