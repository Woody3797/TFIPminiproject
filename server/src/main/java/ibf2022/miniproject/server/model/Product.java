package ibf2022.miniproject.server.model;

import java.io.StringReader;
import java.time.LocalDateTime;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Product {
    
    private Integer productID;
    private String productName;
    private String description;
    private Double price;
    private String username = "admin";
    private LocalDateTime uploadTime;
    
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

    @Override
    public String toString() {
        return "Product [productID=" + productID + ", productName=" + productName + ", description=" + description
                + ", price=" + price + ", username=" + username + ", uploadTime=" + uploadTime + "]";
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
        .add("productID", productID)
        .add("username", username)
        .add("productName", productName)
        .add("description", description)
        .add("price", price)
        .add("uploadTime", uploadTime.toString())
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
