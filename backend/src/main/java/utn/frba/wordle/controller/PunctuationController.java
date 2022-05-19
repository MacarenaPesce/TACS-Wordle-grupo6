package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.model.dto.*;
import utn.frba.wordle.model.pojo.Language;
import utn.frba.wordle.model.pojo.Ranking;
import utn.frba.wordle.model.pojo.State;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.PunctuationService;
import utn.frba.wordle.service.TournamentService;

import java.util.List;

@RestController
@RequestMapping("/api/punctuation")
@CrossOrigin
public class PunctuationController {

    @Autowired
    PunctuationService punctuationService;


    @GetMapping("/todaysResult/{language}")     //TODO agregar tests para esta ruta
    public ResponseEntity<ResultDto> getTodaysResult(@RequestHeader("Authorization") String token, @PathVariable Language language) {
        SessionDto session = AuthService.getSession(token);

        Long result = punctuationService.getTodaysResult(session.getUserId(), language);

        ResultDto dto = ResultDto.builder()
                .userId(session.getUserId())
                .result(result)
                .language(language)
                .build();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
