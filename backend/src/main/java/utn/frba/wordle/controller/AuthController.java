package utn.frba.wordle.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.model.dto.Session;
import utn.frba.wordle.model.http.LoginRequest;
import utn.frba.wordle.model.http.RegisterRequest;
import utn.frba.wordle.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<Session> register(@RequestBody RegisterRequest request) {

        logger.info("Method called: register - Request: {}", request);

        Session response = authService.register(request.getUsername(), request.getPassword(), request.getEmail());

        logger.info("Method called: register - Response: {}", response);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Session> login(@RequestBody LoginRequest request) {

        logger.info("Method called: login - Request: {}", request);

        Session response = authService.login(request.getUsername(), request.getPassword());

        logger.info("Method called: login - Response: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        logger.info("logout method called with no params");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
