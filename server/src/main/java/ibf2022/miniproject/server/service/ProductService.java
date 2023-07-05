package ibf2022.miniproject.server.service;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import ibf2022.miniproject.server.model.Image;
import ibf2022.miniproject.server.model.OrderDetails;
import ibf2022.miniproject.server.model.Product;
import ibf2022.miniproject.server.model.ProductTags;
import ibf2022.miniproject.server.model.UploadImageResponse;
import ibf2022.miniproject.server.repository.ProductRepository;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Value("${IMAGGAAPIKEY}")
    private String IMAGGAAPIKEY;
    
    @Transactional(rollbackFor = Exception.class)
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
        product.setproductStatus("selling");
        productRepository.insertImageDetailsIntoSQL(imageFiles, productID);

        String uploadID = uploadImageImagga(imageFiles[0]).getResult().get("upload_id");
        List<String> tags = getTagsFromImagga(uploadID);
        ProductTags productTags = new ProductTags(productID, tags);
        System.out.println(productTags.toString());
        productRepository.upsertProductTags(productTags, "selling");

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

    public boolean buyProduct(Integer productID, String buyer, String seller) {
        boolean sql = productRepository.buyProduct(productID);
        boolean mongo = productRepository.upsertOrderDetails(productID, buyer, seller, "buy");

        return (sql && mongo);
    }

    public boolean cancelBuyProduct(Integer productID, String buyer, String seller) {
        boolean sql = productRepository.cancelBuyProduct(productID);
        boolean mongo = productRepository.upsertOrderDetails(productID, buyer, seller, "cancel");
        
        return (sql && mongo);
    }

    public boolean acceptOrder(Integer productID, String buyer) {
        boolean sql = productRepository.acceptOrder(productID);
        boolean mongo = productRepository.upsertOrderDetails(productID, buyer, "sold to " + buyer);

        Optional<ProductTags> opt = productRepository.getProductTags(productID);
        if (opt.isPresent()) {
            productRepository.upsertProductTags(opt.get(), "sold");
        }
        
        return (sql && mongo);
    }

    public OrderDetails getOrderDetails(Integer productID) {
        OrderDetails order = productRepository.getOrderDetails(productID);
        if (order != null) {
            return order;
        }
        return null;
    }

    private UploadImageResponse uploadImageImagga(MultipartFile productImage) throws IOException {
        String url = "https://api.imagga.com/v2/uploads";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", IMAGGAAPIKEY);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        String imageData = Base64.getEncoder().encodeToString(productImage.getBytes());
        body.add("image_base64", imageData);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        UploadImageResponse response = restTemplate.postForObject(url, request, UploadImageResponse.class);
        return response;
    }

    private List<String> getTagsFromImagga(String upload_id) {
        String url = UriComponentsBuilder.fromUriString("https://api.imagga.com/v2/tags")
        .queryParam("image_upload_id", upload_id)
        .queryParam("limit", "3")
        .queryParam("threshold", "40.0")
        .build().toUriString();
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", IMAGGAAPIKEY);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        JsonReader jr = Json.createReader(new StringReader(response.getBody()));
        JsonObject jo = jr.readObject();
        JsonArray jArr = jo.getJsonObject("result").getJsonArray("tags");
        List<String> tags = new ArrayList<>();
        for (JsonValue i : jArr) {
            String tag = i.asJsonObject().getJsonObject("tag").getString("en");
            tags.add(tag);
        }
        return tags;
    }

    public ProductTags getProductTags(Integer productID) {
        Optional<ProductTags> opt = productRepository.getProductTags(productID);
        return opt.isPresent() ? opt.get() : null;
    }

    public List<String> getAllProductTags() {
        return productRepository.getAllProductTags();
    }

    public Integer getAllOtherProductsCount(String email) {
        return productRepository.getAllOtherProductsCount(email);
    }
}

// {"result":{"upload_id":"i196fea54afef99fe7fcd71c040UcWjz"},"status":{"text":"","type":"success"}}
// {"result":{"tags":[{"confidence":79.4613265991211,"tag":{"en":"range"}},{"confidence":64.1534194946289,"tag":{"en":"mountain"}},{"confidence":61.3129501342773,"tag":{"en":"mountains"}}]},"status":{"text":"","type":"success"}}