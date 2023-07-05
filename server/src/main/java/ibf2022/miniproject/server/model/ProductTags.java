package ibf2022.miniproject.server.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;


public class ProductTags {
    
    private Integer productID;
    private List<String> tags = new ArrayList<>();
    private String status;
    
    public Integer getProductID() {
        return productID;
    }
    public void setProductID(Integer productID) {
        this.productID = productID;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    public ProductTags() {
    }
    
    public ProductTags(Integer productID, List<String> tags) {
        this.productID = productID;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "ProductTags [productID=" + productID + ", tags=" + tags + ", status=" + status + "]";
    }

    public JsonObject toJson() {
        JsonArrayBuilder jArr = Json.createArrayBuilder();
        for (String tag : tags) {
            jArr.add(tag);
        }
        return Json.createObjectBuilder()
        .add("productID", productID)
        .add("tags", jArr)
        .add("status", status)
        .build();
    }

}
