package com.ingsw.flyingdutchman.repository;

import com.ingsw.flyingdutchman.model.mo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Metodo per trovare una categoria per ID
    Category findByCategoryID(Long categoryID);

    // Metodo per trovare una categoria per nome
    Category findByName(String name);

    // Metodo per ottenere tutte le categorie
    @Override
    List<Category> findAll();

    // Metodo per ottenere tutte le categorie tranne quella "Premium"
    @Query("SELECT c FROM Category c WHERE c.name <> 'Premium'")
    List<Category> findAllExceptPremium();
}
