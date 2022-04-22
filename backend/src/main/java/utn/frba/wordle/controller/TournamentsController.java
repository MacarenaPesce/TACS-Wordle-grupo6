package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import utn.frba.wordle.dto.JoinDto;
import utn.frba.wordle.dto.MemberDto;
import utn.frba.wordle.dto.ResultDto;
import utn.frba.wordle.dto.TournamentDto;
import utn.frba.wordle.dto.TourneysDto;

import utn.frba.wordle.dto.*;
import utn.frba.wordle.model.Language;
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
        TourneysDto dto = tournamentService.listPublicTournaments();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @PostMapping("submitResults")
    public ResponseEntity<ResultDto> submitResults(@RequestBody ResultDto resultDto) {
        ResultDto dto = tournamentService.submitResults(resultDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
