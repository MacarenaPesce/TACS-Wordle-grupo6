package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.http.FindUsersResponse;
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
    public ResponseEntity<FindUsersResponse> findAll(@RequestParam(required = false) String username,
                                                      @RequestParam(required = false) Integer pageNumber,
                                                      @RequestParam(required = false) Integer maxResults) {
        List<UserDto> usersDto;
        Integer totalPages = null;
        if (username == null){
            usersDto = userService.findAll();
        } else if(pageNumber == null || maxResults == null) {
            usersDto = userService.findByName(username);
        }
        else{
            usersDto = userService.findByNameWithPagination(username, maxResults, pageNumber);
            totalPages = userService.totalUserPages(maxResults);
        }
        FindUsersResponse response = buildResponse(pageNumber, maxResults, usersDto, totalPages);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private FindUsersResponse buildResponse(Integer pageNumber, Integer maxResults, List<UserDto> usersDto, Integer totalPages) {
        List<UserResponse> users = usersDto
                .stream().map(this::buildResponse).collect(Collectors.toList());
        return FindUsersResponse.builder()
                .users(users)
                .maxResults(maxResults)
                .pageNumber(pageNumber)
                .totalPages(totalPages)
                .build();
    }

    public UserResponse buildResponse (UserDto dto) {
        return UserResponse.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .build();
    }
}
