package utn.frba.wordle.dto;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class UsuarioDto {

    private Long id;
    private String nombre;
    private String dni;
    private String apellido;
    private String usuario;
    private String email;
    private String legajo;
    private List<Long> idsGrupos;
    private Date fechaUltimoLogin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioDto)) return false;
        UsuarioDto that = (UsuarioDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(dni, that.dni) &&
                Objects.equals(apellido, that.apellido) &&
                Objects.equals(usuario, that.usuario) &&
                Objects.equals(email, that.email) &&
                Objects.equals(legajo, that.legajo) &&
                Objects.equals(idsGrupos, that.idsGrupos) &&
                Objects.equals(fechaUltimoLogin, that.fechaUltimoLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, dni, apellido, usuario, email, legajo, idsGrupos, fechaUltimoLogin);
    }
}
