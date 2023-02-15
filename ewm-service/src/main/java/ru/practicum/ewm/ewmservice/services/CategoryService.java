package ru.practicum.ewm.ewmservice.services;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.ewmservice.dto.CategoryDto;
import ru.practicum.ewm.ewmservice.dto.CategoryNewDto;

import java.util.List;

@Service
public interface CategoryService {
    CategoryDto createCategory(CategoryNewDto categoryNewDto);

    void deleteCategory(Long id);

    CategoryDto updateCategory(CategoryNewDto categoryNewDto, Long id);

    List<CategoryDto> findAllCategories(Integer from, Integer size);

    CategoryDto findCategoryById(Long catId);
}
