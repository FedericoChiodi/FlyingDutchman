package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public Product findByProductID(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> findByOwner(User owner) {
        return productRepository.findByOwner(owner);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> findByOwnerNotDeleted(User user) {
        return productRepository.findByOwnerNotDeleted(user);
    }

    public Product findByAuction(Auction auction) {
        return productRepository.findByAuction(auction);
    }
}
