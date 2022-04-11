package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.UsuarioDto;
import utn.frba.wordle.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @SneakyThrows
    @PostMapping
    public ResponseEntity<String> create(@RequestBody UsuarioDto nuevoUsuario) {
        UsuarioDto usuario = usuarioService.create(nuevoUsuario);
        String respuesta = String.format("Usuario %s creado.", usuario.getId());
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody UsuarioDto usuarioDto) {
        return new ResponseEntity<>(usuarioService.update(usuarioDto), HttpStatus.OK);
    }

    @GetMapping("usuario")
    public ResponseEntity<UsuarioDto> usuarioBuscadoPorID(@RequestParam Long idUsuario) {
       return new ResponseEntity<>(usuarioService.buscarUsuarioDTOporId(idUsuario), HttpStatus.OK);   
    }
}
