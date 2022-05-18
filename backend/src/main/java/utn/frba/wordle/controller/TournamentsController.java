package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.model.Ranking;
import utn.frba.wordle.model.State;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.TournamentService;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@CrossOrigin
public class TournamentsController {

    @Autowired
    TournamentService tournamentService;

    @PostMapping
    public ResponseEntity<TournamentDto> create(@RequestHeader("Authorization") String token, @RequestBody TournamentDto tournamentDto) {

        SessionDto session = AuthService.getSession(token);

        TournamentDto dto = tournamentService.create(tournamentDto, session.getUserId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/{tournamentId}/members/{userId}")
    public ResponseEntity<MemberNewDto> addMember(@RequestHeader("Authorization") String token, @PathVariable Long userId, @PathVariable Long tournamentId) {

       /* if(true){
            throw new BusinessException("Llegue aqui");
        }*/

        SessionDto session = AuthService.getSession(token);

        MemberNewDto dto = tournamentService.addMember(userId, tournamentId, session.getUserId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping ("/{tournamentId}/join")
    public ResponseEntity<JoinDto> join(@RequestHeader("Authorization") String token, @PathVariable Long tournamentId) {
        SessionDto session = AuthService.getSession(token);
        JoinDto dto = tournamentService.join(session.getUserId(), tournamentId);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("public")
    public ResponseEntity<TourneysDto> listPublicTournaments(){
        TourneysDto dto = tournamentService.listPublicTournaments();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @PostMapping("submitResults")
    public ResponseEntity<String> submitResults(@RequestHeader("Authorization") String token, @RequestBody ResultDto resultDto) {
        SessionDto session = AuthService.getSession(token);
        tournamentService.submitResults(session.getUserId(), resultDto);
        return new ResponseEntity<>("Resultados cargados correctamente", HttpStatus.OK);
    }

    @GetMapping("/{tournamentId}/ranking")
    public ResponseEntity<Ranking> getRanking(@PathVariable Long tournamentId) {
        Ranking ranking = tournamentService.getRanking(tournamentId);

        return new ResponseEntity<>(ranking, HttpStatus.OK);
    }

    @GetMapping("/{state}")
    public ResponseEntity<List<TournamentDto>> findUserTournamentsByState(@RequestHeader("Authorization") String token, @PathVariable State state){
        SessionDto session = AuthService.getSession(token);
        List<TournamentDto> tournaments = tournamentService.findUserTournamentsByState(session.getUserId(), state);

        return new ResponseEntity<>(tournaments, HttpStatus.OK);
    }
}
