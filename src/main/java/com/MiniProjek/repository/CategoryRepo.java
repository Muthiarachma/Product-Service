package com.MiniProjek.repository;

import com.MiniProjek.models.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    List<Category> findByNameStartingWith(String prefix);

    List<Category>OrderByNameAsc ();

}
