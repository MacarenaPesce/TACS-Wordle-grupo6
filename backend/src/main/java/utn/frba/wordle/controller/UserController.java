package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.entity.TournamentEntity;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.ResultService;
import utn.frba.wordle.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ResultService resultService;

    @GetMapping ("getPositions")
    public ResponseEntity<PositionsResponseDto> getPositions() {

        PositionsResponseDto dto = userService.getPositions();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping ("/{userId}/tournaments")
    public ResponseEntity<TourneysDto> getMyTournamets(@RequestHeader("Authorization") String token, @PathVariable Long userId) {

        SessionDto session = AuthService.getSession(token); //todo sacar dto de authservice
        checkIDs(session, userId);

        List<TournamentEntity> tournaments = userService.getMyTournamets(userId);


        TourneysDto tourneysDto = TourneysDto.builder()
                .tourneys(mapToDto(tournaments))
                .build();

        return new ResponseEntity<>(tourneysDto, HttpStatus.OK);
    }

    private void checkIDs(SessionDto session, Long userId){
        Long tokenUserId = session.getUserId();
        if (userId != tokenUserId){
            throw new BusinessException("Debe coincidir el user id del path, con el user id del usuario logueado");
        }
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

    @GetMapping("/{userId}/getTodaysResult/{language}")
    public ResponseEntity<ResultDto> getTodaysResult(@RequestHeader("Authorization") String token, @PathVariable Long userId, @PathVariable Language language) {
        SessionDto session = AuthService.getSession(token);
        checkIDs(session, userId);

        Long result = resultService.getTodaysResult(userId, language);

        ResultDto dto = ResultDto.builder()
                .userId(userId)
                .result(result)
                .language(language)
                .build();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
