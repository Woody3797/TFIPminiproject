package ibf2022.miniproject.server.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.miniproject.server.model.Image;
import ibf2022.miniproject.server.model.Product;
import ibf2022.miniproject.server.repository.ProductRepository;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    public Product addNewProduct(Product product, MultipartFile[] imageFiles) throws IOException {
        List<Image> images = new ArrayList<>();

        for (MultipartFile file : imageFiles) {
            Image image = new Image(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            images.add(image);
        }
        int productID = productRepository.insertProductDetailsIntoSQL(product);
        // product.setImages(images);
        product.setUploadTime(LocalDateTime.now());
        product.setProductID(productID);
        productRepository.insertImageDetailsIntoSQL(imageFiles, productID);

        return product;
    }

    public Optional<Product> getProductByID(Integer productID) {
        Optional<Product> opt = productRepository.getProductByID(productID);
        if (opt.isPresent()) {
            return opt;
        } else {
            return Optional.empty();
        }
    }

    public JsonArray getAllProducts(String email, Integer pageSize, Integer pageIndex) {
        Optional<List<Product>> opt = productRepository.getAllProducts(email, pageSize, pageIndex);
        if (opt.isPresent()) {
            List<Product> products = opt.get();
            JsonArrayBuilder jab = Json.createArrayBuilder();

            for (Product p : products) {
                jab.add(p.toJson());
            }
            return jab.build();
        }
        return null;
    }

    public JsonArray getAllOtherProducts(String email, Integer pageSize, Integer pageIndex) {
        Optional<List<Product>> opt = productRepository.getAllOtherProducts(email, pageSize, pageIndex);
        if (opt.isPresent()) {
            List<Product> products = opt.get();
            JsonArrayBuilder jab = Json.createArrayBuilder();

            for (Product p : products) {
                jab.add(p.toJson());
            }
            return jab.build();
        }
        return null;
    }
    
    public Boolean editProduct(Product product, MultipartFile[] imageFiles) throws IOException {
        List<Image> images = new ArrayList<>();

        for (MultipartFile file : imageFiles) {
            Image image = new Image(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            images.add(image);
        }
        product.setImages(images);
        int prodRes = productRepository.editProductDetailsInSQL(product);
        product.setUploadTime(LocalDateTime.now());
        int imageRes = productRepository.editImageDetailsInSQL(imageFiles, product.getProductID());

        return prodRes > 0 && imageRes > 0;
    }

    public boolean deleteProduct(Integer productID) {
        boolean ima = productRepository.deleteImagesByProductID(productID);
        boolean prod = productRepository.deleteProductByID(productID);

        return (ima && prod);
    }

    public boolean buyProduct(Integer productID) {
        return productRepository.buyProduct(productID);
    }

    public boolean cancelBuyProduct(Integer productID) {
        return productRepository.cancelBuyProduct(productID);
    }
}
