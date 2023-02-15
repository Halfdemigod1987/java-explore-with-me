package ru.practicum.ewm.ewmservice.services;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.ewmservice.dto.CompilationDto;
import ru.practicum.ewm.ewmservice.dto.CompilationNewDto;

import java.util.List;

@Service
public interface CompilationService {
    List<CompilationDto> findAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto findCompilation(Long compId);

    CompilationDto createCompilation(CompilationNewDto compilationNewDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(CompilationNewDto compilationNewDto, Long compId);
}
