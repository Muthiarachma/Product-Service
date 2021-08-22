package com.MiniProjek.services;

import com.MiniProjek.models.entities.Category;
import com.MiniProjek.models.entities.Product;
import com.MiniProjek.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.TransactionScoped;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@TransactionScoped
public class CategoryServiceImpl implements CategoriService{

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public Category save (Category category){
        if(category.getId()!=null){
            Category currentCategory = categoryRepo.findById(category.getId()).get();
            currentCategory.setName(category.getName());
            category = currentCategory;
        }
        return categoryRepo.save(category);
    }

    public List<Category> savefromcsvCategory (MultipartFile file){
        try{
            List<Category> categoryList = CSVHelper.csvToCategory (file.getInputStream());
            return categoryRepo.saveAll(categoryList);
        }catch(IOException e){
            throw new RuntimeException("fail to store csv data: "+ e.getMessage());
        }
    }

    public ByteArrayInputStream loadCategory() {
        List<Category> tutorials = categoryRepo.findAll();
        ByteArrayInputStream in = CSVHelper.dbToCSVCategory(tutorials);
        return in;
    }

    @Override
    public Category findOne (Long id) {
        Optional<Category> category = categoryRepo.findById(id);
        if (category.isPresent()) {
            return category.get();
        }
        return null;
    }

    @Override
    public Iterable<Category> findAll(){
        return categoryRepo.findAll();
    }

    @Override
    public void removeOne (Long id){
        categoryRepo.deleteById(id);
    }

    @Override
    public List<Category> findByNameStartingWith (String prefix){
        return categoryRepo.findByNameStartingWith(prefix);
    }

    @Override
    public List<Category> OrderByNameAsc (){
        return categoryRepo.OrderByNameAsc();
    }


}
