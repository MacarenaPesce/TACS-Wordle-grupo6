package utn.frba.wordle.controller;

import ch.qos.logback.classic.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.logging.WordleLogger;
import utn.frba.wordle.model.http.LoginRequest;
import utn.frba.wordle.model.http.RegisterRequest;
import utn.frba.wordle.model.pojo.Session;
import utn.frba.wordle.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    private static final Logger logger = WordleLogger.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequest request) {

        logger.info("Method: register - Request: {}", request);

        Session response = authService.register(request.getUsername(), request.getPassword(), request.getEmail());

        logger.info("Method: register - Response: {}", response);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request) {

        logger.info("Method: login - Request: {}", request);

        Session response = authService.login(request.getUsername(), request.getPassword());

        logger.info("Method: login - Response: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        logger.info("logout Method with no params");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
