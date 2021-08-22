package com.MiniProjek.controllers;

import com.MiniProjek.models.dto.CategoryDTO;
import com.MiniProjek.models.dto.ProductDTO;
import com.MiniProjek.models.dto.SearchDataDTO;
import com.MiniProjek.models.entities.Category;
import com.MiniProjek.services.CSVHelper;
import com.MiniProjek.services.CategoryServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/category")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ProductDTO<Category>> create (@Valid @RequestBody CategoryDTO categoryDTO, Errors errors){
        ProductDTO<Category> productDTO = new ProductDTO<>();

        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()){
                productDTO.getMessage().add(error.getDefaultMessage());
            }
            productDTO.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productDTO);
        }

        Category category = modelMapper.map(categoryDTO, Category.class);
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(categoryServiceImpl.save(category));
        productDTO.setPayload(categoryList);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping
    public Iterable<Category> findAll(){
        return categoryServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public Category findOne (@PathVariable ("id") Long id){
        return categoryServiceImpl.findOne(id);
    }

    @PutMapping
    public ResponseEntity<ProductDTO<Category>> update (@Valid @RequestBody CategoryDTO categoryDTO, Errors errors){
        ProductDTO<Category> productDTO = new ProductDTO<>();

        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()){
                productDTO.getMessage().add(error.getDefaultMessage());
            }
            productDTO.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productDTO);
        }

        Category category = modelMapper.map(categoryDTO, Category.class);
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(categoryServiceImpl.save(category));
        productDTO.setPayload(categoryList);
        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{id}")
    public void removeONE(@PathVariable("id") Long id) {
        categoryServiceImpl.removeOne(id);
    }

    @PostMapping("/search/namestartwith")
    public List<Category> findByNameStartWith(@RequestBody SearchDataDTO searchData){
        return categoryServiceImpl.findByNameStartingWith(searchData.getSearchKey());
    }

    @GetMapping("/order")
    public List<Category> OrderByNameAsc (){
        return categoryServiceImpl.OrderByNameAsc();
    }

    @PostMapping("/upload")
    public ResponseEntity<ProductDTO<Category>> uploadFile(@RequestParam("file") MultipartFile file){
        ProductDTO<Category> productDTO = new ProductDTO();

        if(!CSVHelper.hasCSVFormat(file)){
            productDTO.getMessage().add("Upload file csv");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productDTO);
        }

        try{
            List<Category> categoryList = categoryServiceImpl.savefromcsvCategory(file);
            productDTO.getMessage().add("Upload sukses: "+ file.getOriginalFilename());
            productDTO.setPayload(categoryList);
            return ResponseEntity.ok(productDTO);
        }catch(Exception ex){
            productDTO.getMessage().add("Tidak bisa upload file, coba lagi: "+ file.getOriginalFilename()
            +"\n" + ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productDTO);
        }
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) {
        InputStreamResource file = new InputStreamResource(categoryServiceImpl.loadCategory());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
}
