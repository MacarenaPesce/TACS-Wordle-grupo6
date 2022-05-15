package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.entity.TournamentEntity;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.UserService;

import java.util.*;

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

    @GetMapping ("/{UserId}/tournaments")
    public ResponseEntity<TourneysDto> getMyTournamets(@RequestHeader("Authorization") String token, @PathVariable Long UserId) {

        SessionDto session = AuthService.getSession(token); //todo sacar dto de authservice
        Long tokenUserId = session.getUserId();
        if(UserId != tokenUserId){
            throw new BusinessException("Debe coincidir el user id del path, con el user id del usuario logueado");
        }

        List<TournamentEntity> tournaments = userService.getMyTournamets(tokenUserId);


        TourneysDto tourneysDto = TourneysDto.builder()
                .tourneys(mapToDto(tournaments))
                .build();

        return new ResponseEntity<>(tourneysDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(){
        List<UserDto> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    private TournamentDto mapToDto(TournamentEntity entity) {
        return TournamentDto.builder()
                .language(entity.getLanguage())
                .name(entity.getName())
                .finish(entity.getFinish())
                .start(entity.getStart())
                .state(entity.getState())
                .tourneyId(entity.getId())
                .type(entity.getType())
                .owner(UserService.mapToDto(entity.getOwner()))
                .build();
    }
    private List<TournamentDto> mapToDto(List<TournamentEntity> entities) {
        List<TournamentDto> dtos = new ArrayList<>(Collections.emptySet());
        for(TournamentEntity tournament:entities){
            dtos.add(mapToDto(tournament));
        }
        return dtos;
    }

}
