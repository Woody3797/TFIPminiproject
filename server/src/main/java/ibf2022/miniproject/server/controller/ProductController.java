package ibf2022.miniproject.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.miniproject.server.model.Product;
import ibf2022.miniproject.server.service.ProductService;

@Controller
@RequestMapping
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @PostMapping(path = "/addnewproduct")
    @ResponseBody
    public ResponseEntity<String> addNewProduct(@RequestPart String data, @RequestPart MultipartFile productImage) {
        System.out.println(data);
        try {
            Product product = Product.convertFromJson(data);
            boolean result = productService.addNewProduct(product, productImage);
            return ResponseEntity.ok().body(product.toJson().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{error:" + e.getMessage() + "}");
        }
    }
}
