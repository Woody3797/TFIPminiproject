package ibf2022.miniproject.server.service;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.miniproject.server.model.Product;
import ibf2022.miniproject.server.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    public boolean addNewProduct(Product product, MultipartFile imageFile) throws IOException {
        int sqlRes = productRepository.insertProductDetailsIntoSQL(product.getUsername(), product.getProductName(), product.getDescription(), product.getPrice());

        URL url = productRepository.uploadImageIntoS3(imageFile, product.getUsername());

        return (sqlRes > 0 && url != null);
    }
}
