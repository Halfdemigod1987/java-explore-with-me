package ru.practicum.ewm.ewmservice.dto.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.ewmservice.dto.CategoryDto;
import ru.practicum.ewm.ewmservice.dto.CategoryNewDto;
import ru.practicum.ewm.ewmservice.model.Category;

@Mapper(componentModel = "spring")
@Component
public interface CategoryMapper {

    CategoryDto  categoryToCategoryDto(Category category);

    Category categoryShortDtoToCategory(CategoryNewDto categoryNewDto);
}
