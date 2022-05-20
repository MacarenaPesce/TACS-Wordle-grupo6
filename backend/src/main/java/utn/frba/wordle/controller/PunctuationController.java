package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.model.dto.*;
import utn.frba.wordle.model.http.PunctuationResponse;
import utn.frba.wordle.model.pojo.Language;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.PunctuationService;

@RestController
@RequestMapping("/api/punctuation")
@CrossOrigin
public class PunctuationController {

    @Autowired
    PunctuationService punctuationService;

    @GetMapping("/todaysResult/{language}")
    public ResponseEntity<PunctuationResponse> getTodaysResult(@RequestHeader("Authorization") String token, @PathVariable Language language) {
        SessionDto session = AuthService.getSession(token);

        Long result = punctuationService.getTodaysResult(session.getUserId(), language);

        PunctuationResponse response = PunctuationResponse.builder()
                .language(language)
                .punctuation(result)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
