package utn.frba.wordle.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class UserSession extends UsernamePasswordAuthenticationToken {

    private Long userId;
    private String username;
    private String email;


    public UserSession(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities,
                       Long userId, String username, String email) {
        super(principal, credentials, authorities);

        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public UserSession(Long userId, String username, String email) {
        super(null, null, null);

        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}
