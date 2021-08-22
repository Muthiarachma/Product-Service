package com.MiniProjek.services;

import com.MiniProjek.models.entities.Product;

import java.util.List;


public interface ProductService {
    public Product save (Product product);
    public void removeOne (Long id);
    public Iterable<Product> findAll();
    public Product findOne(Long id);

    public List<Product> findByProductNameLike(String name);
    public List<Product> findByCategory(Long categoryId);
    public List<Product> findProductByPrice(Double price);

}
