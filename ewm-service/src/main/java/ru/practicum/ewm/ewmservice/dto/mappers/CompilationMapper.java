package ru.practicum.ewm.ewmservice.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.ewmservice.dto.CompilationDto;
import ru.practicum.ewm.ewmservice.dto.CompilationNewDto;
import ru.practicum.ewm.ewmservice.model.Compilation;

@Component
@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface CompilationMapper {

    @Mapping(source = "eventsCompilations", target = "events")
    CompilationDto compilationToCompilationDto(Compilation compilation);

    Compilation compilationNewDtoToCompilation(CompilationNewDto compilationNewDto);
}

