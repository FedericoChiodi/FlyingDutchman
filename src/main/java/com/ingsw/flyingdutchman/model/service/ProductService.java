package com.ingsw.flyingdutchman.model.service;

import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(String description, Float minPrice, Float startingPrice, Float currentPrice, String image, Category category, User owner) {
        minPrice = Math.round(minPrice * 100.0f) / 100.0f; // Arrotondamento a 2 decimali
        startingPrice = Math.round(startingPrice * 100.0f) / 100.0f; // Arrotondamento a 2 decimali
        currentPrice = Math.round(currentPrice * 100.0f) / 100.0f; // Arrotondamento a 2 decimali
        Product product = new Product();
        product.setDescription(description);
        product.setMin_price(minPrice);
        product.setStarting_price(startingPrice);
        product.setCurrent_price(currentPrice);
        product.setImage(image);
        product.setDeleted('N');
        product.setCategory(category);
        product.setOwner(owner);
        return productRepository.save(product);
    }

    public void deleteProduct(Product product) {
        product.setDeleted('Y');
        productRepository.save(product);
    }

    public Product findProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> findProductsByOwner(User owner) {
        return productRepository.findByOwner(owner);
    }

    public List<Product> findProductByOwnerNotDeletedNotSold(User owner) {
        return productRepository.findByOwnerAndDeletedFalseNotSold(owner);
    }

    public List<Product> findProductByOwnerNotDeletedSold(User owner) {
        return productRepository.findByOwnerAndDeletedFalseSold(owner);
    }

    public List<Product> findByOwnerNotInAuctions(User owner) {
        return productRepository.findByOwnerNotInAuctions(owner);
    }
}