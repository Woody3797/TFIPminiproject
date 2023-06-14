package ibf2022.miniproject.server.controller;

import ibf2022.miniproject.server.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.miniproject.server.model.User;
import ibf2022.miniproject.server.service.UserService;

@RestController
@RequestMapping(path = "/api")
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping(path = "/user/new")
    public ResponseEntity<UserDto> newUser(@RequestParam("username") String username, @RequestParam("password") String password){
        try {
            userService.signup(username, password);
        } catch (Exception e) {

        }
        return null;
    }
}
