package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.service.UserService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping ("getPositions")
    public ResponseEntity<PositionsResponseDto> getPositions() {

        PositionsResponseDto dto = userService.getPositions();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
