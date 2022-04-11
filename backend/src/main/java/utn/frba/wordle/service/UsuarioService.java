package utn.frba.wordle.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.UsuarioDto;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.UsuarioEntity;
import utn.frba.wordle.repository.UsuarioRepository;
import utn.frba.wordle.utils.ValidadorEmail;

import javax.transaction.Transactional;
import java.security.MessageDigest;
import java.util.*;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioDto create(UsuarioDto usuarioDto) throws BusinessException{

        UsuarioEntity usuarioEntity = usuarioRepository.findByEmail(usuarioDto.getEmail());
        if (usuarioEntity != null) {
                throw new BusinessException("¡Ya existe un registro con el mail ingresado!");
        }
        UsuarioEntity usuarioEntityUsername = usuarioRepository.findByUsername(usuarioDto.getUsuario());
        if (usuarioEntityUsername != null) {
            throw new BusinessException("¡Ya existe un registro con el nombre de Usuario ingresado!");
        }
        UsuarioEntity nuevoUsuario = generoUnNuevoUsuario(usuarioDto);
        nuevoUsuario = usuarioRepository.save(nuevoUsuario);

        return mapToDto(nuevoUsuario);
    }

    private UsuarioDto actualizarUsuarioPorInactividad(UsuarioEntity usuarioEntity, UsuarioDto usuarioDto) {
        usuarioEntity.setFechaAnteriorLogin(new Date());
        usuarioEntity.setFechaUltimoLogin(new Date());
        usuarioEntity.setFechaUltimaActualizacion(new Date());
        usuarioEntity.setUsuario(usuarioDto.getUsuario());
        usuarioEntity.setNombre(usuarioDto.getNombre());
        usuarioEntity.setDni(usuarioDto.getDni());
        usuarioEntity.setApellido(usuarioDto.getApellido());
        usuarioEntity.setLegajo(usuarioDto.getLegajo());
        String passwordDefault = generarPasswordDefault(usuarioDto.getLegajo());
        String passwordHasheada = hashearPassword(passwordDefault);
        usuarioEntity.setPassword(passwordHasheada);

        usuarioEntity = usuarioRepository.save(usuarioEntity);

        return mapToDto(usuarioEntity);
    }

    public void actualizarFechaLogin(UsuarioEntity usuarioEntity) {
        Date ultimoLogin = usuarioEntity.getFechaUltimoLogin();
        usuarioEntity.setFechaAnteriorLogin(Objects.requireNonNullElseGet(ultimoLogin, Date::new));
        usuarioEntity.setFechaUltimoLogin(new Date());
        usuarioRepository.save(usuarioEntity);
    }

    public String update(UsuarioDto usuarioDto) {
        if (usuarioDto.getId() == null) {
            throw new BusinessException("Error al actualizar usuario");
        }
        UsuarioEntity usuarioEntity = usuarioRepository.findById(usuarioDto.getId()).orElseThrow();
        if(!usuarioEntity.getEmail().equals(usuarioDto.getEmail())){
            throw new BusinessException("No es posible modificar el mail de un Usuario");
        }
        usuarioEntity.setNombre(usuarioDto.getNombre());
        usuarioEntity.setDni(usuarioDto.getDni());
        usuarioEntity.setApellido(usuarioDto.getApellido());
        usuarioEntity.setLegajo(usuarioDto.getLegajo());
        usuarioEntity.setUsuario(usuarioDto.getUsuario());
        usuarioEntity.setFechaUltimaActualizacion(new Date());

        usuarioRepository.save(usuarioEntity);

        return "usuario " + usuarioEntity.getId() + " actualizado";
    }

    public UsuarioEntity buscarUsuarioPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new BusinessException("No se encontró el usuario - " + idUsuario));
    }

    public UsuarioDto buscarUsuarioDTOporId(Long idUsuario) {
        return mapToDto(buscarUsuarioPorId(idUsuario));
    }

    public UsuarioEntity buscarUsuarioPorUsernameYPassword(String username, String password) {
        String hashedPassword = hashearPassword(password);
        return usuarioRepository.findByUsernameAndPassword(username, hashedPassword);
    }

    public UsuarioEntity buscarUsuarioPorEmailYLegajo(String email, String legajo) {
        return usuarioRepository.findByEmailAndLegajo(email, legajo);
    }

    public List<UsuarioDto> usuariosNoAsignados(Long idGrupo) {
        List<UsuarioEntity> usuarios = usuarioRepository.obtenerUsuariosNoAsignados(idGrupo);
        return mapToDto(usuarios);
    }

    public List<UsuarioDto> usuariosAsignados(Long idGrupo) {
        List<UsuarioEntity> usuarios = usuarioRepository.obtenerUsuariosAsignados(idGrupo);
        return mapToDto(usuarios);
    }

    public List<UsuarioDto> usuariosDeCurso(Long idGrupo) {
        List<UsuarioEntity> usuarios = usuarioRepository.obtenerUsuariosDeCurso(idGrupo);
        return mapToDto(usuarios);
    }
    
    public List<UsuarioDto> usuariosNoEstudiantes() {
        List<UsuarioEntity> usuarios = usuarioRepository.obtenerUsuariosNoEstudiantes();
        return mapToDto(usuarios);
    }

    public List<UsuarioDto> usuariosNoEstudiantesDelCurso(Long idGrupo) {
        List<UsuarioEntity> usuarios = usuarioRepository.obtenerUsuariosNoEstudiantesDelCurso(idGrupo);
        return mapToDto(usuarios);
    }

    public UsuarioDto cambiarContrasenia(String usuario, String nuevaPass, String viejaPass) {
        UsuarioEntity usuarioEntity = buscarUsuarioPorUsernameYPassword(usuario, viejaPass);
        usuarioEntity.setPassword(hashearPassword(nuevaPass));
        usuarioEntity.setFechaUltimaActualizacion(new Date());
        usuarioEntity = usuarioRepository.save(usuarioEntity);
        return mapToDto(usuarioEntity);
    }

    public UsuarioDto resetContrasenia(String email, String legajo) {
        UsuarioEntity usuarioEntity = buscarUsuarioPorEmailYLegajo(email, legajo);
        String nuevaPass = generarPasswordDefault(legajo);
        usuarioEntity.setPassword(hashearPassword(nuevaPass));
        usuarioEntity.setFechaUltimaActualizacion(new Date());
        usuarioEntity = usuarioRepository.save(usuarioEntity);
        return mapToDto(usuarioEntity);

    }

    public UsuarioEntity generoUnNuevoUsuario(UsuarioDto usuario) {
        if(usuario.getUsuario() == null){
            usuario.setUsuario(usuario.getLegajo());
        }
        if(!ValidadorEmail.esValido(usuario.getEmail())){
            throw new BusinessException(String.format("El mail ingresado para el usuario %s \nno es un mail válido: %s", usuario.getUsuario(), usuario.getEmail()));
        }
        UsuarioEntity nuevoUsuario = mapToEntity(usuario);
        String passwordDefault = generarPasswordDefault(usuario.getLegajo());
        String passwordHasheada = hashearPassword(passwordDefault);
        nuevoUsuario.setPassword(passwordHasheada);
        nuevoUsuario.setFechaCreacion(new Date());
        nuevoUsuario.setFechaUltimaActualizacion(new Date());
        nuevoUsuario.setFechaAnteriorLogin(new Date());
        return nuevoUsuario;
    }

    @SneakyThrows
    public String hashearPassword(String password)  {

        String generatedPassword;
        // Create MessageDigest instance for MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        //Add password bytes to digest
        md.update(password.getBytes());
        //Get the hash's bytes
        byte[] bytes = md.digest();
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        generatedPassword = sb.toString();
        return generatedPassword;
    }

    public String generarPasswordDefault(String legajoUsuario) {
        return Base64.getEncoder().encodeToString(String.format("UTN%s", legajoUsuario).getBytes());
    }

    public static UsuarioEntity mapToEntity(UsuarioDto usuarioDto) {
        if(!ValidadorEmail.esValido(usuarioDto.getEmail())){
            throw new BusinessException(String.format("El mail ingresado para el usuario %s \nno es un mail válido: %s", usuarioDto.getUsuario(), usuarioDto.getEmail()));
        }
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setNombre(usuarioDto.getNombre());
        usuarioEntity.setDni(usuarioDto.getDni());
        usuarioEntity.setId(usuarioDto.getId());
        usuarioEntity.setUsuario(usuarioDto.getUsuario());
        usuarioEntity.setApellido(usuarioDto.getApellido());
        usuarioEntity.setEmail(usuarioDto.getEmail());
        usuarioEntity.setLegajo(usuarioDto.getLegajo());
        usuarioEntity.setFechaAnteriorLogin(usuarioDto.getFechaUltimoLogin());
        return usuarioEntity;
    }


    public static List<UsuarioEntity> mapToEntity(List<UsuarioDto> usuarios) {
        List<UsuarioEntity> usuariosEntity = new ArrayList<>();

        usuarios.forEach(usuario -> {
            UsuarioEntity usuarioDto = mapToEntity(usuario);
            usuariosEntity.add(usuarioDto);
        });

        return usuariosEntity;
    }

    public static List<UsuarioDto> mapToDto(List<UsuarioEntity> usuarios) {
        List<UsuarioDto> usuariosDto = new ArrayList<>();

        usuarios.forEach(usuario -> {
            UsuarioDto usuarioDto = mapToDto(usuario);
            usuariosDto.add(usuarioDto);
        });

        return usuariosDto;
    }

    //TODO refactorizar, deberia estar hecho con el builder
    public static UsuarioDto mapToDto(UsuarioEntity usuario) {
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setId(usuario.getId());
        usuarioDto.setNombre(usuario.getNombre());
        usuarioDto.setDni(usuario.getDni());
        usuarioDto.setApellido(usuario.getApellido());
        usuarioDto.setUsuario(usuario.getUsuario());
        usuarioDto.setEmail(usuario.getEmail());
        usuarioDto.setLegajo(usuario.getLegajo());
        usuarioDto.setFechaUltimoLogin(usuario.getFechaUltimoLogin());
        return usuarioDto;
    }
}
