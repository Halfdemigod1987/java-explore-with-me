package ru.practicum.ewm.ewmservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.ewmservice.Utils;
import ru.practicum.ewm.ewmservice.dao.CompilationRepository;
import ru.practicum.ewm.ewmservice.dao.EventRepository;
import ru.practicum.ewm.ewmservice.dto.CompilationDto;
import ru.practicum.ewm.ewmservice.dto.CompilationNewDto;
import ru.practicum.ewm.ewmservice.dto.mappers.CompilationMapper;
import ru.practicum.ewm.ewmservice.exceptions.CompilationNotFoundException;
import ru.practicum.ewm.ewmservice.exceptions.EventNotFoundException;
import ru.practicum.ewm.ewmservice.model.Compilation;
import ru.practicum.ewm.ewmservice.model.Event;
import ru.practicum.ewm.ewmservice.services.CompilationService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    private Compilation findCompilationById(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new CompilationNotFoundException(
                        String.format("Compilation with id=%d was not found", id)));
    }

    private Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(
                        String.format("Event with id=%d was not found", id)));
    }

    @Override
    public List<CompilationDto> findAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable;
        if (size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageable = PageRequest.of(from / size, size);
        }

        if (pinned != null) {
            return compilationRepository.findAllByPinned(pinned, pageable)
                    .stream()
                    .map(compilationMapper::compilationToCompilationDto)
                    .collect(Collectors.toList());
        } else {
            return compilationRepository.findAll(pageable)
                    .stream()
                    .map(compilationMapper::compilationToCompilationDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public CompilationDto findCompilation(Long compId) {
        return compilationMapper.compilationToCompilationDto(findCompilationById(compId));
    }

    @Override
    public CompilationDto createCompilation(CompilationNewDto compilationNewDto) {
        Compilation compilation = compilationMapper.compilationNewDtoToCompilation(compilationNewDto);
        compilation.setEventsCompilations(new ArrayList<>());
        compilationNewDto.getEvents()
                .forEach(eventId -> compilation.getEventsCompilations().add(findEventById(eventId)));
        return compilationMapper.compilationToCompilationDto(
                compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = findCompilationById(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(CompilationNewDto compilationNewDto, Long compId) {
        Compilation compilation = findCompilationById(compId);
        copyProperties(compilationNewDto, compilation, Utils.getNullPropertyNames(compilationNewDto));
        compilationNewDto.getEvents()
                .forEach(eventId -> {
                    if (compilation.getEventsCompilations()
                            .stream()
                            .filter(event -> event.getId().equals(eventId))
                            .findFirst()
                            .isEmpty()) {
                        compilation.getEventsCompilations().add(findEventById(eventId));
                    }
                    });
        return compilationMapper.compilationToCompilationDto(
                compilationRepository.save(compilation));
    }


}
