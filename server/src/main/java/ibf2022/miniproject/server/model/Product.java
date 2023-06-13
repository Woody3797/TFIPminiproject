package ibf2022.miniproject.server.model;

import java.io.StringReader;
import java.sql.Date;
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
    private String username = "admin";
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
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
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

    public Product(Integer productID, String productName, String description, Double price, String username,
            LocalDateTime uploadTime, List<Image> images) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.username = username;
        this.uploadTime = uploadTime;
        this.images = images;
    }

    public Product(Integer productID, String productName, String description, Double price, String username,
            LocalDateTime uploadTime) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.username = username;
        this.uploadTime = uploadTime;
    }
    
    @Override
    public String toString() {
        return "Product [productID=" + productID + ", productName=" + productName + ", description=" + description
                + ", price=" + price + ", username=" + username + ", uploadTime=" + uploadTime + ", images=" + images + "]";
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
        .add("username", username)
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
