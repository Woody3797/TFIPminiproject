package ibf2022.miniproject.server.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.miniproject.server.model.Product;
import ibf2022.miniproject.server.service.ProductService;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @PostMapping(path = "/addnewproduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addNewProduct(@RequestPart("product") Product product, @RequestPart("productImages") MultipartFile[] productImage) {
        try {
            Product result = productService.addNewProduct(product, productImage);
            System.out.println(result);
            return ResponseEntity.ok().body(result.toJson().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(path = "/product/{productID}")
    public ResponseEntity<String> getProductByID(@PathVariable String productID) {
        Optional<Product> opt = productService.getProductByID(Integer.parseInt(productID));

        if (opt.isPresent()) {
            return ResponseEntity.ok().body(opt.get().toJson().toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{error : no images found}");
        }
    }

    
}
