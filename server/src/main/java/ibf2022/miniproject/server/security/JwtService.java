package ibf2022.miniproject.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final static String SECRET_KEY = "6e636945574e5373736b7851683039746a50564b4e346b38554f386d6f4a6832";
  public String extractUsername(String jwt) {
    return extractClaim(jwt, Claims::getSubject);
  }
  private Claims extractAllClaims(String jwt){
    return (Claims) Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parse(jwt)
        .getBody();
  }

  public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver){
    final Claims claims = extractAllClaims(jwt);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails){
    return generateToken(new HashMap<>(),userDetails);
  }
  public String generateToken(Map<String, Object> extractClaims,
      UserDetails userDetails){
    return Jwts.builder()
        .setClaims(extractClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
        .signWith(SignatureAlgorithm.ES256, getSigningKey())
        .compact();
  }

  public boolean isTokenValid(String jwt, UserDetails userDetails){
    final String username = extractUsername(jwt);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
  }

  private boolean isTokenExpired(String jwt) {
    return extractExperiation(jwt).before(new Date());
  }

  private Date extractExperiation(String jwt) {
    return extractClaim(jwt, Claims::getExpiration);
  }

  private byte[] getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes).getEncoded();
  }
}
