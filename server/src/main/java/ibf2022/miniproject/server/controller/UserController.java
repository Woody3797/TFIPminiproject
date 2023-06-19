package ibf2022.miniproject.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.miniproject.server.model.Login;
import ibf2022.miniproject.server.model.Signup;
import ibf2022.miniproject.server.model.User;
import ibf2022.miniproject.server.service.JwtService;
import ibf2022.miniproject.server.service.UserService;
import jakarta.json.Json;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;
    
    
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody Login loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            ResponseCookie jwtCookie = jwtService.generateCookieFromUsername(user.getUsername());
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(user.toJson().toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Json.createObjectBuilder().add("error", "Username/Password not found or incorrect").build().toString());
        }
    }

    
    @PostMapping(path = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signup(@RequestBody Signup signupRequest) throws Exception {
        System.out.println(signupRequest.toString());
        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(Json.createObjectBuilder().add("error", "Email already registered: " + signupRequest.getEmail()).build().toString());
        } 
        try {
            userService.loadUserByUsername(signupRequest.getUsername());
            return ResponseEntity.badRequest().body(Json.createObjectBuilder().add("error", "Username already registered: " + signupRequest.getUsername()).build().toString());
        } catch (UsernameNotFoundException e) {
            signupRequest.setPassword(encoder.encode(signupRequest.getPassword()));
            boolean result = userService.signupNewUser(signupRequest);
            if (result) {
                return ResponseEntity.status(HttpStatus.CREATED).body(Json.createObjectBuilder().add("result", "User registered: " + signupRequest.getUsername()).build().toString());
            } else {
                return ResponseEntity.badRequest().body(Json.createObjectBuilder().add("error", "Unable to register user").build().toString());
            }
        }
    }


}
