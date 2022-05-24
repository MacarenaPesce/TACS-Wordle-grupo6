package utn.frba.wordle.security;

import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import utn.frba.wordle.service.AuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer";
    private final List<String> excludeUrlPatterns = new ArrayList<>();

    private Claims validateToken(HttpServletRequest request) {
        String SECRET = AuthService.SECRET;
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    // aca agregar las URL publicas que no requieren autorizacion
    public JWTAuthorizationFilter() {
        excludeUrlPatterns.add("/api/auth/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            if (existeJWTToken(request, response)) {
                Claims claims = validateToken(request);
                if (claims != null && !claims.isEmpty()) {
                    setUpSpringAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            chain.doFilter(request, response);

          }
            catch (ExpiredJwtException ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            } catch (UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

    /**
     * Metodo para autenticarnos dentro del flujo de Spring
     *
     */
    private void setUpSpringAuthentication(Claims claims) {
        @SuppressWarnings("unchecked")
        List<String> authorities = null;
        Long userId = Long.valueOf((Integer) claims.get("userId"));
        String username = (String) claims.get("username");
        String email = (String) claims.get("email");

        UserSession auth = new UserSession(claims.getSubject(), null, null, userId, username, email);
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    private boolean existeJWTToken(HttpServletRequest request, HttpServletResponse res) {
        String authenticationHeader = request.getHeader(HEADER);
        return authenticationHeader != null && authenticationHeader.startsWith(PREFIX);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest requestFilter) {

        return excludeUrlPatterns.stream()
                .anyMatch(p -> requestFilter.getRequestURL().toString().contains(p));
    }

}
