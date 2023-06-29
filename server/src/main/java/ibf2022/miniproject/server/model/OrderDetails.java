package ibf2022.miniproject.server.model;

import java.util.Arrays;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class OrderDetails {
    
    private String id;
    private Integer productID;
    private String seller;
    private String[] buyers;
    private String status;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Integer getProductID() {
        return productID;
    }
    public void setProductID(Integer productID) {
        this.productID = productID;
    }
    public String getSeller() {
        return seller;
    }
    public void setSeller(String seller) {
        this.seller = seller;
    }
    public String[] getBuyers() {
        return buyers;
    }
    public void setBuyers(String[] buyers) {
        this.buyers = buyers;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public OrderDetails() {
    }
    
    public OrderDetails(String id, Integer productID, String seller, String[] buyers, String status) {
        this.id = id;
        this.productID = productID;
        this.seller = seller;
        this.buyers = buyers;
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderDetails [id=" + id + ", productID=" + productID + ", seller=" + seller + ", buyers=" + Arrays.toString(buyers) + ", status=" + status + "]";
    }

    public JsonObject toJson() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (String buyer : buyers) {
            jab.add(buyer);
        }
        return Json.createObjectBuilder()
        .add("id", id)
        .add("productID", productID)
        .add("seller", seller)
        .add("buyers", jab)
        .add("status", status)
        .build();
    }

}
