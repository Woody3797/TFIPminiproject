package ibf2022.miniproject.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.miniproject.server.service.UserService;
import jakarta.json.Json;

@RestController
@RequestMapping(path = "/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/editprofile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> editProfile(@RequestPart("email") String email, @RequestPart(value = "password", required = false) String password, @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        try {
            String result = userService.editProfileDetails(email, password, profileImage);

            return ResponseEntity.ok().body(Json.createObjectBuilder().add("result", result).build().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(Json.createObjectBuilder().add("error", "unable to edit profile").build().toString());
        }
    }

    @GetMapping(path = "/getprofilepic/{email}")
    public ResponseEntity<String> getProfilePic(@PathVariable String email) {
        String url = userService.getProfilePic(email);
        System.out.println(url);
        if (url != null) {
            return ResponseEntity.ok().body(Json.createObjectBuilder().add("url", url).build().toString());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Json.createObjectBuilder().add("error", "unable to get picture").build().toString());
    }

    @DeleteMapping(path = "/deleteprofilepic/{email}")
    public ResponseEntity<String> deleteProfilePic(@PathVariable String email) {
        if (userService.deleteProfilePic(email)) {
            return ResponseEntity.ok().body(Json.createObjectBuilder().add("result", "picture successfully deleted").build().toString());
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(Json.createObjectBuilder().add("error", "unable to delete picture").build().toString());
    }
}
