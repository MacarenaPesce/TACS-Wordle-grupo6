package utn.frba.wordle.security;

import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

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
        String SECRET = "mySecretKey";
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
                if (claims.get("authorities") != null) {
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
        List<String> authorities = (List<String>) claims.get("authorities");
        String usuario = (String) claims.get("usuario");
        Long idUsuario = Long.valueOf((Integer) claims.get("idUsuario"));
        String email = (String) claims.get("email");
        String nombre = (String) claims.get("nombre");
        String apellido = (String) claims.get("apellido");
        String legajo = (String) claims.get("legajo");
        String dni = (String) claims.get("dni");

        SessionUser auth = new SessionUser(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()), idUsuario, usuario, email, nombre, apellido, legajo, dni);
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
