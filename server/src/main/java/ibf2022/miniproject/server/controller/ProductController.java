package ibf2022.miniproject.server.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.miniproject.server.model.OrderDetails;
import ibf2022.miniproject.server.model.Product;
import ibf2022.miniproject.server.model.UploadImageResponse;
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
            System.out.println(e.getMessage());
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

    @GetMapping(path = "/{email}/productlist")
    public ResponseEntity<String> getAllProductsByEmail(@PathVariable String email, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false, defaultValue = "0") Integer pageIndex) {
        JsonArray jArr = productService.getAllProducts(email, pageSize, pageIndex);
        
        if (jArr != null) {
            return ResponseEntity.ok().body(jArr.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Json.createObjectBuilder().add("error", "no products found").build().toString());
        }
    }

    @GetMapping(path = "/{email}/allproducts")
    public ResponseEntity<String> getAllOtherProducts(@PathVariable String email, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false, defaultValue = "0") Integer pageIndex) {
        JsonArray jArr = productService.getAllOtherProducts(email, pageSize, pageIndex);
        
        if (jArr != null) {
            return ResponseEntity.ok().body(jArr.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Json.createObjectBuilder().add("error", "no products found").build().toString());
        }
    }

    @PutMapping(path = "/editproduct/{productID}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @PostMapping(path = "/buyproduct")
    public ResponseEntity<String> buyProduct(@RequestParam MultiValueMap<String, String> data) {
        System.out.println(data);
        String status = data.getFirst("status");
        String productID = data.getFirst("productID");
        String buyer = data.getFirst("buyer");
        String seller = data.getFirst("seller");
        if (status != null && status.contains("pending")) {
            if (productService.buyProduct(Integer.parseInt(productID), buyer, seller)) {
                OrderDetails order = productService.getOrderDetails(Integer.parseInt(productID));
                return ResponseEntity.ok().body(order.toJson().toString());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Json.createObjectBuilder().add("error", "unable to complete transaction").build().toString());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Json.createObjectBuilder().add("error", "unable to complete transaction").build().toString());
    }

    @PostMapping(path = "/cancelpending")
    public ResponseEntity<String> cancelBuyProduct(@RequestParam MultiValueMap<String, String> data) {
        System.out.println(data);
        String productID = data.getFirst("productID");
        String buyer = data.getFirst("buyer");
        String seller = data.getFirst("seller");
        if (productService.cancelBuyProduct(Integer.parseInt(productID), buyer, seller)) {
            OrderDetails order = productService.getOrderDetails(Integer.parseInt(productID));
            return ResponseEntity.ok().body(order.toJson().toString());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Json.createObjectBuilder().add("error", "unable to cancel pending order").build().toString());
        }
    }

    @PostMapping(path = "/acceptorder")
    public ResponseEntity<String> acceptOrder(@RequestParam MultiValueMap<String, String> data) {
        System.out.println(data);
        String productID = data.getFirst("productID");
        String buyer = data.getFirst("buyer");
        if (productService.acceptOrder(Integer.parseInt(productID), buyer)) {
            OrderDetails order = productService.getOrderDetails(Integer.parseInt(productID));
            return ResponseEntity.ok().body(order.toJson().toString());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Json.createObjectBuilder().add("error", "unable to accept order").build().toString());
        }        
    }

    @GetMapping(path = "/getorderdetails/{productID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrderDetails(@PathVariable String productID) {
        OrderDetails order = productService.getOrderDetails(Integer.parseInt(productID));
        if (order != null) {
            return ResponseEntity.ok().body(order.toJson().toString());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Json.createObjectBuilder().add("error", "no order details yet").build().toString());
    }



    @PostMapping(path = "/uploadimageimagga", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImageImagga(@RequestPart("productImage") MultipartFile productImage) throws IOException {

        UploadImageResponse response = productService.uploadImageImagga(productImage);
        String upload_id = response.getResult().get("upload_id");
        List<String> result = productService.getTagsFromImagga(upload_id);
        System.out.println(result);
        
        return ResponseEntity.ok().body(result.toString());
    }
}
