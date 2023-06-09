package ibf2022.miniproject.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.miniproject.server.model.User;
import ibf2022.miniproject.server.service.UserService;
import jakarta.annotation.PostConstruct;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping(path = "/createNewUser")
    public User createNewUser(@RequestBody User user) {
        return userService.createNewUser(user);
    }

    @GetMapping(path = "/forAdmin")
    public String forAdmin() {
        return "URL for admin access only";
    }

    @GetMapping(path = "/forUser")
    public String forUser() {
        return "URL for users only";
    }

    @PostConstruct
    public void initRolesAndUser() {
        userService.initRolesAndUser();
    }
}
