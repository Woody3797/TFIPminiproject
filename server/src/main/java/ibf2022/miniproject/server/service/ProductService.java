package ibf2022.miniproject.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.miniproject.server.model.Product;
import ibf2022.miniproject.server.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    public void addNewProduct(Product product) {
        
    }
}
