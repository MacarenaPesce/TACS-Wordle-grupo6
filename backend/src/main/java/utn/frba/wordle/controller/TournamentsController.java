package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.TournamentService;

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

    @PostMapping("addmember")
    public ResponseEntity<MemberDto> addMember(@RequestHeader("Authorization") String token, @RequestBody MemberDto memberDto) {

        SessionDto session = AuthService.getSession(token);

        MemberDto dto = tournamentService.addMember(memberDto, session.getUserId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/{tournamentId}/join")
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
    public ResponseEntity<ResultDto> submitResults(@RequestBody ResultDto resultDto) {
        ResultDto dto = tournamentService.submitResults(resultDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
