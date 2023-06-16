package ibf2022.miniproject.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.miniproject.server.model.User;
import ibf2022.miniproject.server.service.UserService;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;
    
    

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody User userData) {
        System.out.println(userData);
        return ResponseEntity.ok().body("{\"status\": \"qwer\"}");
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<String> signup(@RequestBody User userData) {
        System.out.println(userData);
        return ResponseEntity.ok().body("{\"status\": \"asdf\"}");
    }
}
