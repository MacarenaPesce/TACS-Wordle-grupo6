package utn.frba.wordle.controller;

import com.google.gson.Gson;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

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

    @GetMapping("/endOfTheDay")
    public ResponseEntity<Object> getEndOfTheDay(@RequestHeader("Authorization") String token){

        logger.info("Method: endOfTheDay - Request: token={}", token);

        Gson gson = new Gson();

        Object response = gson.toJson(LocalDate.now().plusDays(1).atStartOfDay().toString());

        logger.info("Method: endOfTheDay - Response: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/dayOfTheDate")
    public ResponseEntity<Object> getDayOfTheDate(@RequestHeader("Authorization") String token){

        logger.info("Method: getDayOfTheDate - Request: token={}", token);

        Gson gson = new Gson();

        Object response = gson.toJson(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));

        logger.info("Method: getDayOfTheDate - Response: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
