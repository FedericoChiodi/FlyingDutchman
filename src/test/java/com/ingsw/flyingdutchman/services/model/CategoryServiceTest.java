package com.ingsw.flyingdutchman.services.model;

import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.service.CategoryService;
import com.ingsw.flyingdutchman.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindCategoryById() {
        Category category = new Category();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.findCategoryById(1L);

        assertNotNull(result);
        assertEquals(category, result);
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllCategories() {
        Category category1 = new Category();
        Category category2 = new Category();
        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals(categories, result);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllCategoriesExceptPremium() {
        Category category1 = new Category();
        Category category2 = new Category();
        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryRepository.findAllByCategoryIDNot(1L)).thenReturn(categories);

        List<Category> result = categoryService.getAllCategoriesExceptPremium();

        assertEquals(2, result.size());
        assertEquals(categories, result);
        verify(categoryRepository, times(1)).findAllByCategoryIDNot(1L);
    }
}
