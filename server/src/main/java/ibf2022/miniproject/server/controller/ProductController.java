package ibf2022.miniproject.server.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.miniproject.server.model.Product;
import ibf2022.miniproject.server.service.ProductService;
import jakarta.json.Json;
import jakarta.json.JsonArray;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @PostMapping(path = "/addnewproduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewProduct(@RequestPart("product") Product product, @RequestPart("productImages") MultipartFile[] productImages) {
        try {
            Product result = productService.addNewProduct(product, productImages);
            System.out.println(result);
            return ResponseEntity.ok().body(result.toJson().toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Json.createObjectBuilder().add("error", "unable to add product").build().toString());
        }
    }

    @GetMapping(path = "/product/{productID}")
    public ResponseEntity<String> getProductByID(@PathVariable String productID) {
        Optional<Product> opt = productService.getProductByID(Integer.parseInt(productID));

        if (opt.isPresent()) {
            return ResponseEntity.ok().body(opt.get().toJson().toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Json.createObjectBuilder().add("error", "no product found").build().toString());
        }
    }

    @GetMapping(path = "/{username}/productlist")
    public ResponseEntity<String> getAllProducts(@PathVariable String username, @RequestParam(required = false) String limit) {
        JsonArray jArr = productService.getAllProducts(username);

        if (jArr != null) {
            return ResponseEntity.ok().body(jArr.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Json.createObjectBuilder().add("error", "no products found").build().toString());
        }
    }

    @PostMapping(path = "/editproduct/{productID}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> editProduct(@PathVariable String productID, @RequestPart("product") Product product, @RequestPart("productImages") MultipartFile[] productImages) {
        product.setProductID(Integer.parseInt(productID));
        try {
            boolean res = productService.editProduct(product, productImages);
            if (res) {
                Product result = productService.getProductByID(Integer.parseInt(productID)).get();
                return ResponseEntity.ok().body(result.toJson().toString());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(Json.createObjectBuilder().add("error", "unable to edit product details").build().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Json.createObjectBuilder().add("error", "unable to edit product details").build().toString());
        }
    }

    @DeleteMapping(path = "/deleteproduct/{productID}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productID) {
        boolean result = productService.deleteProduct(Integer.parseInt(productID));

        if (result) {
            return ResponseEntity.ok().body(Json.createObjectBuilder().add("result", "product with productID: " + productID + " deleted").build().toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Json.createObjectBuilder().add("error", "unable to delete product").build().toString());
        }
    }
}
