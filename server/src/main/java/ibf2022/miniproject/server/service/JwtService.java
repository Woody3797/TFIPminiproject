package ibf2022.miniproject.server.service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {

    private static final String SECRET_KEY = "8BkysfgvfqBD4pHdZx8x6i2o1j6Qr3KQz0QKqmwRkdUipYTK7cliEuwGDbDnYvOj";
    // private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String getUsernameFromJwt(String jwtToken) {
        System.out.println(jwtToken);
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwtToken).getBody();
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        System.out.println(claims);
        return claimsResolver.apply(claims);
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000*9999999))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwtToken);
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
        Cookie cookie = WebUtils.getCookie(request, "jwt");
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public ResponseCookie generateCookieFromUsername(String username) {
        String jwtToken = generateTokenFromUsername(username);
        ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken).path("/").httpOnly(true).build();
        return cookie;
    }

    
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
}
