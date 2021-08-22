package com.MiniProjek.services;

import com.MiniProjek.models.entities.Category;
import com.MiniProjek.models.entities.Product;
import com.MiniProjek.repository.CategoryRepo;
import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {

    @Autowired
    private CategoriService categoriService;

    private static final String TYPE= "text/csv";

    public static boolean hasCSVFormat(MultipartFile file){
        if(!TYPE.equals(file.getContentType())){
            return false;
        }
        return true;
    }

    public List<Product> csvToProduct (InputStream is){
        try{
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            CSVParser parser = new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            List<Product> productList = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = parser.getRecords();

            for(CSVRecord csvRecord: csvRecords){
                Product product = new Product();
                product.setId(Long.parseLong(csvRecord.get("Id")));
                product.setName(csvRecord.get("Name"));
                product.setDescription(csvRecord.get("Description"));
                product.setPrice(Double.parseDouble(csvRecord.get("Price")));
//                product.setCategory(Long.parseLong(csvRecord.get("Category_id")));
//                Category category = categoriService.findOne(Long.parseLong(csvRecord.get("category_id")));
//                product.setCategory(categoriService.findOne(Long.parseLong(csvRecord.get("Category_id"))));
                productList.add(product);
            }
            parser.close();
            return productList;
        }catch(IOException ex){
            throw new RuntimeException("fail to parse CSV file: "+ ex.getMessage());
        }
    }

//    public Category findCategoryFindById (Long id){
//        return categoriService.findOne(id);
//    }

    public static ByteArrayInputStream dbToCSVProduct (List<Product> productList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            for (Product product : productList) {
                List<String> data = Arrays.asList(
                        String.valueOf(product.getId()),
                        product.getName(),
                        product.getDescription(),
                        String.valueOf(product.getPrice()),
                        String.valueOf(product.getCategory())
                );
                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }

    public static List<Category> csvToCategory (InputStream is){
        try{
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            CSVParser parser = new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            List<Category> categoryList = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = parser.getRecords();

            for(CSVRecord csvRecord: csvRecords){
                Category category = new Category();
                category.setId(Long.parseLong(csvRecord.get("Id")));
                category.setName(csvRecord.get("Name"));
                categoryList.add(category);
            }
            parser.close();
            return categoryList;
        }catch(IOException ex){
            throw new RuntimeException("fail to parse CSV file: "+ ex.getMessage());
        }
    }

    public static ByteArrayInputStream dbToCSVCategory (List<Category> categoryList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            for (Category category : categoryList) {
                List<String> data = Arrays.asList(
                        String.valueOf(category.getId()),
                        category.getName()
                );
                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
