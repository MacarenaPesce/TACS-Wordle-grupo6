package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.http.UserResponse;
import utn.frba.wordle.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(@RequestParam(required = false) String username) {
        List<UserDto> usersDto;
        if (username==null){
            usersDto = userService.findAll();
        } else{
            usersDto = userService.findByName(username);
        }
        List<UserResponse> users = usersDto
                .stream().map(this::buildResponse).collect(Collectors.toList());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    public UserResponse buildResponse (UserDto dto) {
        return UserResponse.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .build();
    }
}
