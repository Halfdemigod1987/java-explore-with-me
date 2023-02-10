package ru.practicum.ewm.ewmservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.ewmservice.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
