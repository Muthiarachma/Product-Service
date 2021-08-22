package com.MiniProjek.services;

import com.MiniProjek.models.entities.Product;
import com.MiniProjek.repository.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    public ProductServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Product save (Product product) {
        return productRepo.save(product);
    }

    public List<Product> savefromcsv (MultipartFile file){
        try{
            CSVHelper csvHelper = new CSVHelper();
            List<Product> productList = csvHelper.csvToProduct(file.getInputStream());
            return productRepo.saveAll(productList);
        }catch(IOException e){
            throw new RuntimeException("fail to store csv data: "+ e.getMessage());
        }
    }

    public ByteArrayInputStream load() {
        List<Product> tutorials = productRepo.findAll();
        ByteArrayInputStream in = CSVHelper.dbToCSVProduct(tutorials);
        return in;
    }

    @Override
    public Product findOne(Long id) {
        Optional<Product> product = productRepo.findById(id);
        if(!product.isPresent()){
            return null;
        }
        return product.get();
    }

    @Override
    public Iterable<Product> findAll() {
        return productRepo.findAll();
    }

    @Override
    public void removeOne (Long id){
        productRepo.deleteById(id);
    }

    @Override
    public List<Product> findByProductNameLike(String name){
        return productRepo.findProductByNameLike("%"+ name +"%");
    }

    @Override
    public List<Product> findByCategory(Long categoryId){
        return productRepo.findProductByCategory(categoryId);
    }

    @Override
    public List<Product> findProductByPrice(Double price){
        return productRepo.findProductByPrice(price);
    }
}