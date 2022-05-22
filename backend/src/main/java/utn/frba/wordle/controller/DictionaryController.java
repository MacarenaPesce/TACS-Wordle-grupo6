package utn.frba.wordle.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.model.http.DefinitionResponse;
import utn.frba.wordle.model.pojo.Language;
import utn.frba.wordle.service.DictionaryService;

import java.util.List;


@RestController
@RequestMapping("/api/dictionary")
@CrossOrigin
public class DictionaryController {

    @Autowired
    DictionaryService dictionaryService;

    private static final Logger logger = LoggerFactory.getLogger(DictionaryController.class);

    @GetMapping("/{language}/{word}")
    public ResponseEntity<DefinitionResponse> getDefinitions(@PathVariable Language language, @PathVariable String word) {

        logger.info("Method: getDefinitions - Request: language={}, word={}", language, word);

        List<String> definitions = dictionaryService.getDefinitions(language, word);

        DefinitionResponse response = DefinitionResponse.builder()
                .definitions(definitions)
                .build();

        logger.info("Method: getDefinitions - Response: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

