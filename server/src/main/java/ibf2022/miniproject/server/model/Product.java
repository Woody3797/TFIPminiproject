package ibf2022.miniproject.server.model;

import java.time.LocalDateTime;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Product {
    
    private Integer productID;
    private String name;
    private String description;
    private Double price;
    private LocalDateTime uploadedTime;
    
    public Integer getProductID() {
        return productID;
    }
    public void setProductID(Integer productID) {
        this.productID = productID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public LocalDateTime getUploadedTime() {
        return uploadedTime;
    }
    public void setUploadedTime(LocalDateTime uploadedTime) {
        this.uploadedTime = uploadedTime;
    }

    @Override
    public String toString() {
        return "Product [productID=" + productID + ", name=" + name + ", description=" + description + ", price="
                + price + ", uploadedTime=" + uploadedTime + "]";
    }
    
    public JsonObject toJson() {
        return Json.createObjectBuilder()
        .add("productID", productID)
        .add("name", name)
        .add("description", description)
        .add("price", price)
        .add("uploadedTime", uploadedTime.toString())
        .build();
    }
}
