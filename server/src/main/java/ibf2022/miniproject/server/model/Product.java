package ibf2022.miniproject.server.model;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Product {
    
    private Integer productID;
    private String productName;
    private String description;
    private Double price;
    private String email;
    private LocalDateTime uploadTime;
    private List<Image> images = new ArrayList<>();
    
    public Integer getProductID() {
        return productID;
    }
    public void setProductID(Integer productID) {
        this.productID = productID;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDateTime getUploadTime() {
        return uploadTime;
    }
    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }
    public List<Image> getImages() {
        return images;
    }
    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Product() {
    }

    public Product(Integer productID, String productName, String description, Double price, String email,
            LocalDateTime uploadTime, List<Image> images) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.email = email;
        this.uploadTime = uploadTime;
        this.images = images;
    }

    public Product(Integer productID, String productName, String description, Double price, String email,
            LocalDateTime uploadTime) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.email = email;
        this.uploadTime = uploadTime;
    }
    
    @Override
    public String toString() {
        return "Product [productID=" + productID + ", productName=" + productName + ", description=" + description + ", price=" + price + ", email=" + email + ", uploadTime=" + uploadTime + ", images=" + images + "]";
    }

    public JsonObject toJson() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (Image i : images) {
            jab.add(i.toJson());
        }

        return Json.createObjectBuilder()
        .add("productID", productID)
        .add("productName", productName)
        .add("description", description)
        .add("price", price)
        .add("email", email)
        .add("uploadTime", uploadTime.toString())
        .add("images", jab)
        .build();
    }

    public static Product convertFromJson(String json) {
        Product product = new Product();
        JsonReader jr = Json.createReader(new StringReader(json));
        JsonObject jo = jr.readObject();
        product.setProductName(jo.getString("productName"));
        product.setPrice(Double.parseDouble(jo.get("price").toString()));
        product.setDescription(jo.getString("description"));
        return product;
    }
    
}
