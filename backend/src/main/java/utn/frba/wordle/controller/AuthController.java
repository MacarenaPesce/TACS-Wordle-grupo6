package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.model.dto.LoginDto;
import utn.frba.wordle.model.dto.SessionDto;
import utn.frba.wordle.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SessionDto> register(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(authService.register(loginDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<SessionDto> login(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(authService.login(loginDto), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
