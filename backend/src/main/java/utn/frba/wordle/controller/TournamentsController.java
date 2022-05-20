package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.model.dto.*;
import utn.frba.wordle.model.http.RegistrationResponse;
import utn.frba.wordle.model.http.RankingResponse;
import utn.frba.wordle.model.http.TournamentResponse;
import utn.frba.wordle.model.pojo.Punctuation;
import utn.frba.wordle.model.pojo.State;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.TournamentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tournaments")
@CrossOrigin
public class TournamentsController {

    @Autowired
    TournamentService tournamentService;

    @PostMapping
    public ResponseEntity<TournamentResponse> create(@RequestHeader("Authorization") String token, @RequestBody TournamentDto tournamentDto) {
        SessionDto session = AuthService.getSession(token);
        TournamentDto dto = tournamentService.create(tournamentDto, session.getUserId());

        TournamentResponse tournament = new TournamentResponse(dto);

        return new ResponseEntity<>(tournament, HttpStatus.OK);
    }

    @GetMapping("/myTournaments")
    public ResponseEntity<List<TournamentResponse>> getTournamentsFromUser(@RequestHeader("Authorization") String token){
        SessionDto session = AuthService.getSession(token);
        List<TournamentDto> tournamentsDto = tournamentService.getTournamentsFromUser(session.getUserId());

        List<TournamentResponse> tournaments = tournamentsDto
                .stream().map(TournamentResponse::new).collect(Collectors.toList());
        return new ResponseEntity<>(tournaments, HttpStatus.OK);
    }

    @PostMapping("/{tournamentId}/members/{userId}")
    public ResponseEntity<RegistrationResponse> addMember(@RequestHeader("Authorization") String token, @PathVariable Long userId, @PathVariable Long tournamentId) {
        SessionDto session = AuthService.getSession(token);
        RegistrationDto dto = tournamentService.addMember(userId, tournamentId, session.getUserId());

        RegistrationResponse registration = RegistrationResponse.builder()
                .tournamentId(dto.getTournamentId())
                .username(dto.getUser().getUsername())
                .build();

        return new ResponseEntity<>(registration, HttpStatus.OK);
    }

    @PostMapping ("/{tournamentId}/join")
    public ResponseEntity<RegistrationResponse> join(@RequestHeader("Authorization") String token, @PathVariable Long tournamentId) {
        SessionDto session = AuthService.getSession(token);
        RegistrationDto dto = tournamentService.join(session.getUserId(), tournamentId);

        RegistrationResponse registration = RegistrationResponse.builder()
                .tournamentId(dto.getTournamentId())
                .username(dto.getUser().getUsername())
                .build();

        return new ResponseEntity<>(registration, HttpStatus.OK);
    }

    @GetMapping("public")
    public ResponseEntity<List<TournamentResponse>> listPublicTournaments(){

        List<TournamentDto> tournamentsDto = tournamentService.listPublicTournaments();

        List<TournamentResponse> tournaments = tournamentsDto
                .stream().map(TournamentResponse::new).collect(Collectors.toList());

        return new ResponseEntity<>(tournaments, HttpStatus.OK);
    }
    
    @PostMapping("submitResults")
    public ResponseEntity<String> submitResults(@RequestHeader("Authorization") String token, @RequestBody ResultDto resultDto) {
        SessionDto session = AuthService.getSession(token);
        tournamentService.submitResults(session.getUserId(), resultDto);
        return new ResponseEntity<>("Resultados cargados correctamente", HttpStatus.OK);
    }

    @GetMapping("/{tournamentId}/ranking")
    public ResponseEntity<RankingResponse> getRanking(@PathVariable Long tournamentId) {
        List<Punctuation> orderedPunctuations = tournamentService.orderedPunctuations(tournamentId);

        RankingResponse ranking = RankingResponse.builder()
                .idTournament(tournamentId)
                .punctuations(orderedPunctuations)
                .build();

        return new ResponseEntity<>(ranking, HttpStatus.OK);
    }

    @GetMapping("/{state}")
    public ResponseEntity<List<TournamentDto>> findUserTournamentsByState(@RequestHeader("Authorization") String token, @PathVariable State state){
        SessionDto session = AuthService.getSession(token);
        List<TournamentDto> tournaments = tournamentService.findUserTournamentsByState(session.getUserId(), state);

        return new ResponseEntity<>(tournaments, HttpStatus.OK);
    }
}
