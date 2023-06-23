package ibf2022.miniproject.server.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import ibf2022.miniproject.server.model.GoogleAuth;
import ibf2022.miniproject.server.service.JwtService;
import ibf2022.miniproject.server.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // String jwtToken = jwtService.getJwtFromCookie(request);
            String jwtToken;
            String googleToken = request.getHeader("Authorization");
            if (googleToken.contains("Google-Bearer")) {
                jwtToken = googleToken.replace("Google-Bearer ", "");
                String data = jwtService.extractDataFromJWT(jwtToken);
                GoogleAuth googleAuth = GoogleAuth.createFromJson(data);
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + googleAuth.toString());
                String email = googleAuth.getEmail();
                filterChain.doFilter(request, response);
                return;
            }

            jwtToken = tokenExtractor(request);
            System.out.println("------------------------------------");
            System.out.println("jwttoken: "+ jwtToken);

            if (jwtToken == null) {
                filterChain.doFilter(request, response);
                return;
            }
            String username = jwtService.getUsernameFromJwt(jwtToken);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(username);
                if (jwtService.validateToken(jwtToken)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    System.out.println(authToken);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // Set bearer token to response headers
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken.toString());
            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            System.out.println("unable to get jwt token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("token has expired: " + e.getMessage());
        }
    }

    public String tokenExtractor(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        System.out.println("header: " + header);
        if (header != null) {
            return header.replace("Bearer ", "");
        }
        Cookie cookie = WebUtils.getCookie(request, "JWTtoken");
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
}
