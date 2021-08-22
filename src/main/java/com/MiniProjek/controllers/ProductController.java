package com.MiniProjek.controllers;


import com.MiniProjek.models.dto.ProductDTO;
import com.MiniProjek.models.dto.SearchDataDTO;
import com.MiniProjek.models.entities.Category;
import com.MiniProjek.models.entities.Product;
import com.MiniProjek.services.CSVHelper;
import com.MiniProjek.services.ProductService;
import com.MiniProjek.services.ProductServiceImpl;
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
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping
    public ResponseEntity<ProductDTO<Product>> create(@Valid @RequestBody Product product, Errors errors) {

        ProductDTO<Product> productDTO = new ProductDTO<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                productDTO.getMessage().add(error.getDefaultMessage());
            }
            productDTO.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productDTO);
        }
        List<Product> productList = new ArrayList<>();
        productList.add(productService.save(product));
        productDTO.setPayload(productList);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping
    public Iterable<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product findOne(@PathVariable("id") Long id) {
        return productService.findOne(id);
    }

    @PutMapping
    public ResponseEntity<ProductDTO<Product>> update(@Valid @RequestBody Product product, Errors errors) {
        ProductDTO<Product> productDTO = new ProductDTO<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                productDTO.getMessage().add(error.getDefaultMessage());
            }
            productDTO.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productDTO);
        }
        List<Product> productList = new ArrayList<>();
        productList.add(productService.save(product));
        productDTO.setPayload(productList);
        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{id}")
    public void removeONE(@PathVariable("id") Long id) {
        productService.removeOne(id);
    }

    @PostMapping("/search/namelike")
    public List<Product> getProductByNameLike(@RequestBody SearchDataDTO searchDataDTO) {
        return productService.findByProductNameLike(searchDataDTO.getSearchKey());
    }

    @GetMapping("/search/category/{categoryId}")
    public List<Product> getProductByCategory(@PathVariable("categoryId") Long categoryId) {
        return productService.findByCategory(categoryId);
    }

    @GetMapping("/order-price")
    public List<Product> findProductByPrice(Double price){
        return productService.findProductByPrice(price);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
        ProductDTO<Product> productDTO = new ProductDTO<>();

        if(!CSVHelper.hasCSVFormat(file)){
            productDTO.getMessage().add("Upload file csv");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productDTO);
        }

        try{
            List<Product> productList = productService.savefromcsv(file);
            productDTO.getMessage().add("Upload sukses: "+ file.getOriginalFilename());
            productDTO.setPayload(productList);
            return ResponseEntity.ok(productDTO);
        }catch(Exception ex){
            productDTO.getMessage().add("Tidak bisa upload file, coba lagi: "+ file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(productDTO);
        }
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) {
        InputStreamResource file = new InputStreamResource(productService.load());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
}
