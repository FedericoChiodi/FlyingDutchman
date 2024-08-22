package com.ingsw.flyingdutchman.services.model;

import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.ProductService;
import com.ingsw.flyingdutchman.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct() {
        String description = "Sample Product";
        Float minPrice = 50.123f;
        Float startingPrice = 100.456f;
        Float currentPrice = 120.789f;
        String image = "image.jpg";
        Category category = new Category();
        User owner = new User();

        Product product = new Product();
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(description, minPrice, startingPrice, currentPrice, image, category, owner);

        assertNotNull(result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product();
        product.setDeleted('N');

        productService.deleteProduct(product);

        assertEquals('Y', product.getDeleted());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testFindProductById() {
        Product product = new Product();
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Product result = productService.findProductById(1L);

        assertNotNull(result);
        assertEquals(product, result);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindProductsByOwner() {
        User owner = new User();
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findByOwner(owner)).thenReturn(products);

        List<Product> result = productService.findProductsByOwner(owner);

        assertEquals(2, result.size());
        assertEquals(products, result);
        verify(productRepository, times(1)).findByOwner(owner);
    }

    @Test
    public void testFindProductByOwnerNotDeletedNotSold() {
        User owner = new User();
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findByOwnerAndDeletedFalseNotSold(owner)).thenReturn(products);

        List<Product> result = productService.findProductByOwnerNotDeletedNotSold(owner);

        assertEquals(2, result.size());
        assertEquals(products, result);
        verify(productRepository, times(1)).findByOwnerAndDeletedFalseNotSold(owner);
    }

    @Test
    public void testFindProductByOwnerNotDeletedSold() {
        User owner = new User();
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findByOwnerAndDeletedFalseSold(owner)).thenReturn(products);

        List<Product> result = productService.findProductByOwnerNotDeletedSold(owner);

        assertEquals(2, result.size());
        assertEquals(products, result);
        verify(productRepository, times(1)).findByOwnerAndDeletedFalseSold(owner);
    }

    @Test
    public void testFindByOwnerNotInAuctions() {
        User owner = new User();
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findByOwnerNotInAuctions(owner)).thenReturn(products);

        List<Product> result = productService.findByOwnerNotInAuctions(owner);

        assertEquals(2, result.size());
        assertEquals(products, result);
        verify(productRepository, times(1)).findByOwnerNotInAuctions(owner);
    }
}
