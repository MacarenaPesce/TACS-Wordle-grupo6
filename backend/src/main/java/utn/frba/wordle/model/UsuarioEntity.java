package utn.frba.wordle.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Apellido")
    private String apellido;

    @Column(name = "Usuario", unique = true)
    private String usuario;

    @Column(name = "Dni")
    private String dni;

    @Column(name = "Password")
    private String password;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "Legajo")
    private String legajo;

    @Column(name = "Fecha_Ultimo_Login")
    private Date fechaUltimoLogin;

    @Column(name = "Fecha_Anterior_Login")
    private Date fechaAnteriorLogin;

    @Column(name = "Fecha_Creacion")
    private Date fechaCreacion;

    @Column(name = "Fecha_Ultima_Actualizacion")
    private Date fechaUltimaActualizacion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioEntity)) return false;
        UsuarioEntity that = (UsuarioEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(apellido, that.apellido) &&
                Objects.equals(usuario, that.usuario) &&
                Objects.equals(password, that.password) &&
                Objects.equals(email, that.email) &&
                Objects.equals(legajo, that.legajo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido, usuario, password, email, legajo);
    }
}
