package utn.frba.wordle.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.SessionDto;
import utn.frba.wordle.dto.TournamentDto;

import java.util.Date;

@RestController
@RequestMapping("/api/tournaments")
@CrossOrigin
public class TournamentsController {

    @PostMapping
    public ResponseEntity<SessionDto> create(@RequestBody TournamentDto tournamentDto) {
        TournamentDto.builder()
                .name("Pepita")
                .language("ES")
                .tourneyId(1)
                .type("Public")
                .finish(new Date())
                .start(new Date())
                .build();
        return null;
    }
}
