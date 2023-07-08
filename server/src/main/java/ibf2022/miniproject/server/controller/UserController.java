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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.miniproject.server.model.GoogleAuth;
import ibf2022.miniproject.server.model.Login;
import ibf2022.miniproject.server.model.Signup;
import ibf2022.miniproject.server.model.User;
import ibf2022.miniproject.server.service.EmailService;
import ibf2022.miniproject.server.service.JwtService;
import ibf2022.miniproject.server.service.UserService;
import jakarta.json.Json;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody Login loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            ResponseCookie jwtCookie = jwtService.generateCookieFromEmail(user.getEmail());
            String jwtToken = "Bearer " + jwtService.generateTokenFromEmail(user.getEmail());
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).header(HttpHeaders.AUTHORIZATION, jwtToken).body(user.toJson().toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Json.createObjectBuilder().add("error", "Email/Password not found or incorrect").build().toString());
        }
    }

    @PostMapping(path = "/googlelogin")
    public ResponseEntity<String> loginByGoogle(@RequestBody String googleAuthRequest) {
        try {
            String payload = jwtService.extractDataFromJWT(googleAuthRequest);
            GoogleAuth googleAuth = GoogleAuth.createFromJson(payload);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(googleAuth.getEmail(), googleAuth.getSub()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            String jwtToken = "Bearer " + jwtService.generateTokenFromEmail(user.getEmail());
            ResponseCookie jwtCookie = jwtService.generateCookieFromEmail(user.getEmail());
            System.out.println(">>>>>>>>>>>>-------- " + authentication.toString() + jwtToken);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).header(HttpHeaders.AUTHORIZATION, jwtToken).body(user.toJson().toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Json.createObjectBuilder().add("error", e.getMessage()).build().toString());
        }
    }

    @PostMapping(path = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signup(@RequestBody Signup signupRequest) throws Exception {
        System.out.println(signupRequest.toString());
        try {
            if (userService.existsByEmail(signupRequest.getEmail())) {
                return ResponseEntity.badRequest().body(Json.createObjectBuilder().add("error", "Email already registered: " + signupRequest.getEmail()).build().toString());
            } else {
                signupRequest.setPassword(jwtService.passwordEncoder().encode(signupRequest.getPassword()));
                boolean result = userService.signupNewUser(signupRequest.getEmail(), signupRequest.getPassword());
                if (result) {
                    User user = (User) userService.loadUserByUsername(signupRequest.getEmail());
                    return ResponseEntity.status(HttpStatus.CREATED).body(user.toJson().toString());
                } else {
                    return ResponseEntity.badRequest().body(Json.createObjectBuilder().add("error", "Unable to register user").build().toString());
                }
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Json.createObjectBuilder().add("error", "Unable to register user").build().toString());
        }
    }

    @DeleteMapping(path = "/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        SecurityContextHolder.clearContext();
        String jwtToken = "Bearer " + jwtService.generateTokenFromEmail(null);
        Cookie cookie = new Cookie("JWTtoken", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        // ResponseCookie cookie = ResponseCookie.from("JWTtoken", null).path("/").httpOnly(true).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).header(HttpHeaders.AUTHORIZATION, jwtToken).body(Json.createObjectBuilder().add("Result", "Successfully logged out").build().toString());
    }

    @PostMapping(path = "/resetpassword")
    public ResponseEntity<String> resetPassword(@RequestBody String email) throws MessagingException {
        System.out.println(email);
        String password = jwtService.generateResetPassword();
        if (userService.resetPassword(email, password)) {
            emailService.sendEmailFromTemplate("wuchienwei89@gmail.com", password);
            return ResponseEntity.ok().body(Json.createObjectBuilder().add("status", "Password reset, check email for details").build().toString());
        }
        System.out.println("no email found");
        return ResponseEntity.badRequest().body(Json.createObjectBuilder().add("error", "Unable to reset password").build().toString());
    }

}
