package com.ingsw.flyingdutchman.repository;

import com.ingsw.flyingdutchman.model.mo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    List<Category> findAllByCategoryIDNot(Long categoryID);
}
