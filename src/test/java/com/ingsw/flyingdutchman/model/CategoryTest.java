package com.ingsw.flyingdutchman.model;

import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    private Category category;
    private List<Product> productList;

    @BeforeEach
    public void setup() {
        category = new Category();
        productList = new ArrayList<>();
    }

    @Test
    public void testGetSetCategoryID() {
        Long categoryID = 1L;
        category.setCategoryID(categoryID);
        assertEquals(categoryID, category.getCategoryID());
    }

    @Test
    public void testGetSetName() {
        String name = "Electronics";
        category.setName(name);
        assertEquals(name, category.getName());
    }

    @Test
    public void testGetSetProducts() {
        Product product1 = new Product();
        Product product2 = new Product();
        productList.add(product1);
        productList.add(product2);

        category.setProducts(productList);
        assertEquals(productList, category.getProducts());
        assertEquals(2, category.getProducts().size());
    }

}
