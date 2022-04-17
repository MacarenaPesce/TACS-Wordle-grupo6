package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.service.TournamentService;

import java.util.Collections;
import java.util.Date;

@RestController
@RequestMapping("/api/tournaments")
@CrossOrigin
public class TournamentsController {

    @Autowired
    TournamentService tournamentService;

    @PostMapping
    public ResponseEntity<TournamentDto> create(@RequestBody TournamentDto tournamentDto) {
        TournamentDto dto = tournamentService.create(tournamentDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("addmember")
    public ResponseEntity<MemberDto> addMember(@RequestBody MemberDto memberDto) {
        MemberDto dto = tournamentService.addMember(memberDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<JoinDto> join(@PathVariable Integer id) {
        JoinDto dto = tournamentService.join(id);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("public")
    public ResponseEntity<TourneysDto> listPublicTournaments(){
        TournamentDto tournamentDto = TournamentDto.builder()
                .name("Pepita")
                .language("ES")
                .tourneyId(1)
                .type("Public")
                .finish(new Date())
                .start(new Date())
                .build();

        TourneysDto dto = TourneysDto.builder()
                .tourneys(Collections.singletonList(tournamentDto))
                .build();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @PostMapping("submitResults")
    public ResponseEntity<ResultDto> submitResults(@RequestBody ResultDto resultDto) {
        ResultDto dto =  ResultDto.builder()
                .result(2)
                .languaje("EN")
                .build();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
