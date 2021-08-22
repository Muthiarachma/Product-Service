package com.MiniProjek.services;

import com.MiniProjek.models.entities.Category;

import java.util.List;

public interface CategoriService {
    public Category save (Category category);
    public Category findOne (Long id);
    public Iterable<Category> findAll();
    public void removeOne (Long id);

    public List<Category> findByNameStartingWith (String prefix);
    public List<Category> OrderByNameAsc ();
}
