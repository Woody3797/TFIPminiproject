package ibf2022.miniproject.server.model;

import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class UploadImageResponse {
    
    private Map<String, String> result;
    private Map<String, Object> status;
    
    public Map<String, String> getResult() {
        return result;
    }
    public void setResult(Map<String, String> result) {
        this.result = result;
    }
    public Map<String, Object> getStatus() {
        return status;
    }
    public void setStatus(Map<String, Object> status) {
        this.status = status;
    }
    
    public UploadImageResponse() {
    }
    
    public UploadImageResponse(Map<String, String> result, Map<String, Object> status) {
        this.result = result;
        this.status = status;
    }

    @Override
    public String toString() {
        return "UploadImageResponse [result=" + result + ", status=" + status + "]";
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
        .add("upload_id", result.get("upload_id"))
        .build();
    }
    
}
