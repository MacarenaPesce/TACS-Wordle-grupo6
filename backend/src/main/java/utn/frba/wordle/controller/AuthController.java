package utn.frba.wordle.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.model.dto.LoginDto;
import utn.frba.wordle.model.dto.SessionDto;
import utn.frba.wordle.model.http.LoginRequest;
import utn.frba.wordle.model.http.RegisterRequest;
import utn.frba.wordle.service.AuthService;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    //private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<SessionDto> register(@RequestBody RegisterRequest request) {

        SessionDto sessionDto = authService.register(request.getUsername(), request.getPassword(), request.getEmail());

        return new ResponseEntity<>(sessionDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<SessionDto> login(@RequestBody LoginRequest loginRequest) {

        //logger.info("GetDefinitions - Login called with params: {}", loginRequest);

        SessionDto sessionDto = authService.login(loginRequest.getUsername(), loginRequest.getPassword());

        return new ResponseEntity<>(sessionDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
