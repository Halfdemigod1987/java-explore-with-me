package ru.practicum.ewm.ewmservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.ewmservice.dto.CompilationDto;
import ru.practicum.ewm.ewmservice.dto.CompilationNewDto;
import ru.practicum.ewm.ewmservice.services.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> createCompilation(@Valid @RequestBody CompilationNewDto compilationNewDto) {
        return new ResponseEntity<>(compilationService.createCompilation(compilationNewDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@RequestBody CompilationNewDto compilationNewDto,
                                                            @PathVariable Long compId) {
        return ResponseEntity.ok(compilationService.updateCompilation(compilationNewDto, compId));
    }

}
