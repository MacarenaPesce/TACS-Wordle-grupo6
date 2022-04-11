package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import utn.frba.wordle.model.UsuarioEntity;

import java.util.List;

public interface UsuarioRepository extends CrudRepository<UsuarioEntity, Long> {

    @Query(value = "SELECT * FROM usuario u WHERE u.email = :email", nativeQuery = true)
    UsuarioEntity findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM usuario u WHERE u.usuario = :username and (u.estado = 'ACTIVO' or u.estado is null) and u.password  = :password", nativeQuery = true)
    UsuarioEntity findByUsernameAndPassword(String username, String password);

    @Query(value = "SELECT * FROM usuario u WHERE u.email = :email and u.legajo = :legajo and (u.estado = 'ACTIVO' or u.estado is null)", nativeQuery = true)
    UsuarioEntity findByEmailAndLegajo(String email, String legajo);

    @Query(value = "SELECT u.* FROM usuario u, grupo_usuario gu WHERE u.id = gu.id_usuario and gu.id_grupo = :id", nativeQuery = true)
    List<UsuarioEntity> obtenerUsuariosAsignados(Long id);

    @Query(value = "select u1.* from usuario u1 ", nativeQuery = true)
    List<UsuarioEntity> obtenerUsuariosNoAsignados(@Param("idGrupo") Long idGrupo);

    @Query(value = "SELECT u.* FROM usuario u, curso_entity_docentes cu WHERE u.id = cu.docentes and (u.estado = 'ACTIVO' or u.estado is null) and cu.curso_entity_id = :id", nativeQuery = true)
    List<UsuarioEntity> obtenerUsuariosDeCurso(Long id);

    @Query(value = "SELECT u.* \n" +
            "FROM usuario u, grupo_usuario gu \n" +
            "WHERE (u.estado = 'ACTIVO' or u.estado is null) \n" +
            "and gu.id_usuario = u.id and gu.id_grupo = :idGrupo\n", nativeQuery = true)
    List<UsuarioEntity> obtenerUsuariosNoEstudiantesDelCurso(Long idGrupo);

    @Query(value = "SELECT * FROM usuario u WHERE u.usuario = :username and (estado = 'ACTIVO' or estado is null)", nativeQuery = true)
    UsuarioEntity findByUsername(String username);

    @Query(value = "SELECT u.* FROM usuario u", nativeQuery = true)
    List<UsuarioEntity> obtenerUsuariosNoEstudiantes();

    @Modifying
    @Query(value = "DELETE FROM grupo_usuario WHERE id_usuario = :idUsuario", nativeQuery = true)
    void desasociarDeTodosLosGrupos(Long idUsuario);
}
