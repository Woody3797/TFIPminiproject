package ibf2022.miniproject.server.service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {

    private static final String SECRET_KEY = "bf8bc08e34578e5d6ffc996271ebb5a9db182a1b179e810019cfc1d00bc0b17e";
    // private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String getEmailFromJwt(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwtToken).getBody();
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    public String extractDataFromJWT(String jwtToken) {
        String[] chunks = jwtToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String data = new String(decoder.decode(chunks[1]));
        return data;
    }

    public String generateTokenFromEmail(String email) {
        return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000*3600*24*5))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwtToken);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty");
        }
        return false;
    }

    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "JWTtoken");
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public ResponseCookie generateCookieFromEmail(String email) {
        String jwtToken = generateTokenFromEmail(email);
        ResponseCookie cookie = ResponseCookie.from("JWTtoken", jwtToken).path("/").httpOnly(true).build();
        // Cookie cookie = new Cookie("JWTtoken", jwtToken);
        // cookie.setPath("/");
        // cookie.setMaxAge(86400);
        // cookie.setHttpOnly(true);
        return cookie;
    }

    public String generateResetPassword() {
        String password = UUID.randomUUID().toString().substring(0, 8);
        return password;
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
