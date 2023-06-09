package ibf2022.miniproject.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2022.miniproject.server.model.Product;
import ibf2022.miniproject.server.service.ProductService;

@Controller
@RequestMapping
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @PostMapping(path = "/addnewproduct")
    public void addNewProduct(@RequestBody Product product) {

    }
}
