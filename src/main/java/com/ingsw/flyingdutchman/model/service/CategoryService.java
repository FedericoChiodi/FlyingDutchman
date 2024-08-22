package com.ingsw.flyingdutchman.model.service;

import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findCategoryById(Long categoryID) {
        return categoryRepository.findById(categoryID).orElse(null);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getAllCategoriesExceptPremium() {
        return categoryRepository.findAllByCategoryIDNot(1L);
    }
}