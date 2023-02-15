package ru.practicum.ewm.ewmservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.ewmservice.Utils;
import ru.practicum.ewm.ewmservice.dao.CategoryRepository;
import ru.practicum.ewm.ewmservice.dao.EventRepository;
import ru.practicum.ewm.ewmservice.dto.CategoryDto;
import ru.practicum.ewm.ewmservice.dto.CategoryNewDto;
import ru.practicum.ewm.ewmservice.dto.mappers.CategoryMapper;
import ru.practicum.ewm.ewmservice.exceptions.CategoryDeleteDeniedException;
import ru.practicum.ewm.ewmservice.exceptions.CategoryNotFoundException;
import ru.practicum.ewm.ewmservice.model.Category;
import ru.practicum.ewm.ewmservice.services.CategoryService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CategoryNewDto categoryNewDto) {
        return categoryMapper.categoryToCategoryDto(
                categoryRepository.save(
                        categoryMapper.categoryShortDtoToCategory(categoryNewDto)));
    }

    @Override
    public void deleteCategory(Long id) {
        findCategory(id);
        if (eventRepository.findAllByCategoryId(id).size() > 0) {
            throw new CategoryDeleteDeniedException("The category is not empty");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryNewDto categoryNewDto, Long id) {
        Category category = findCategory(id);

        copyProperties(categoryNewDto, category, Utils.getNullPropertyNames(categoryNewDto));

        return categoryMapper.categoryToCategoryDto(
                categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> findAllCategories(Integer from, Integer size) {
        Pageable pageable;
        if (size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageable = PageRequest.of(from / size, size);
        }

        return categoryRepository.findAll(pageable)
                .stream()
                .map(categoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findCategoryById(Long catId) {
        return categoryMapper.categoryToCategoryDto(findCategory(catId));
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        String.format("Category with id=%d was not found", id)));
    }
}
