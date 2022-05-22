package utn.frba.wordle.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.model.http.PunctuationResponse;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.pojo.Session;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.PunctuationService;

@RestController
@RequestMapping("/api/punctuation")
@CrossOrigin
public class PunctuationController {

    @Autowired
    PunctuationService punctuationService;

    private static final Logger logger = LoggerFactory.getLogger(PunctuationController.class);

    @GetMapping("/todaysResult/{language}")
    public ResponseEntity<PunctuationResponse> getTodaysResult(@RequestHeader("Authorization") String token, @PathVariable Language language) {
        logger.info("Method: getTodaysResult - Request: token={}, language={}", token, language);

        Session session = AuthService.getSession(token);

        Long result = punctuationService.getTodaysResult(session.getUserId(), language);

        PunctuationResponse response = PunctuationResponse.builder()
                .language(language)
                .punctuation(result)
                .build();

        logger.info("Method: getDefinitions - Response: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
