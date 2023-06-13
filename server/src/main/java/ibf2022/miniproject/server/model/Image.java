package ibf2022.miniproject.server.model;

import java.util.Base64;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Image {
    
    private Integer imageID;
    private String imageName;
    private String type;
    private byte[] imageBytes;
    
    public Integer getImageID() {
        return imageID;
    }
    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }
    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public byte[] getImageBytes() {
        return imageBytes;
    }
    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
    
    public Image() {
    }

    public Image(String imageName, String type, byte[] imageBytes) {
        this.imageName = imageName;
        this.type = type;
        this.imageBytes = imageBytes;
    }
    
    public Image(Integer imageID, String imageName, String type, byte[] imageBytes) {
        this.imageID = imageID;
        this.imageName = imageName;
        this.type = type;
        this.imageBytes = imageBytes;
    }

    public JsonObject toJson() {
        String encoded = Base64.getEncoder().encodeToString(imageBytes);
        
        return Json.createObjectBuilder()
        .add("imageID", imageID)
        .add("imageName", imageName)
        .add("type", type)
        .add("imageBytes", encoded)
        .build();
    }
}
