package utn.frba.wordle.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class SessionUser extends UsernamePasswordAuthenticationToken {

    private Long idUsuario;
    private String usuario;
    private String email;
    private String nombre;
    private String apellido;
    private String legajo;
    private String dni;



    public SessionUser(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities,
                       Long idUsuario, String usuario, String email, String nombre, String apellido, String legajo, String dni) {
        super(principal, credentials, authorities);

        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.legajo = legajo;
        this.dni = dni;
    }

    public SessionUser(Long idUsuario, String usuario, String email, String nombre, String apellido, String legajo, String dni) {
        super(null, null, null);

        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.legajo = legajo;
        this.dni = dni;
    }
}
