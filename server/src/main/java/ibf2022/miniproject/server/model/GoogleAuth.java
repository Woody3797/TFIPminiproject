package ibf2022.miniproject.server.model;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class GoogleAuth {

    private String email;
    private String name;
    private String picture;
    private String sub;
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getSub() {
        return sub;
    }
    public void setSub(String sub) {
        this.sub = sub;
    }
    
    public GoogleAuth() {
    }
    
    public GoogleAuth(String email, String name, String picture, String sub) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.sub = sub;
    }

    @Override
    public String toString() {
        return "GoogleAuth [email=" + email + ", name=" + name + ", picture=" + picture + ", sub=" + sub + "]";
    }

    public static GoogleAuth createFromJson(String data) {
        JsonReader jr = Json.createReader(new StringReader(data));
        JsonObject jo = jr.readObject();
        GoogleAuth g = new GoogleAuth(jo.getString("email"), jo.getString("name"), jo.getString("picture"), jo.getString("sub"));
        return g;
    }
}

// {"iss":"https://accounts.google.com","nbf":1687458654,"aud":"216028312066-dll1brg9sq28s4lmi1vsi1n0u9gvptoa.apps.googleusercontent.com","sub":"104929009696232405315","email":"woody3797@gmail.com","email_verified":true,"azp":"216028312066-dll1brg9sq28s4lmi1vsi1n0u9gvptoa.apps.googleusercontent.com","name":"Wu Chien Wei","picture":"https://lh3.googleusercontent.com/a/AAcHTtcwb1mCoIhm_yiylCDy0Xy6XkHAnD0QdJfU8txOQQ=s96-c","given_name":"Wu","family_name":"Chien Wei","iat":1687458954,"exp":1687462554,"jti":"7e105fbfa4b3ef96772bc7adc42f82c0457b1d27"}
