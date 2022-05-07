package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.DictionaryDto;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.service.DictionaryService;

import java.util.List;


@RestController
@RequestMapping("/api/dictionary")
@CrossOrigin
public class DictionaryController {

    @Autowired
    DictionaryService dictionaryService;

    @GetMapping("/{language}/{word}")
    public ResponseEntity<DictionaryDto> getDefinitions(@PathVariable Language language, @PathVariable String word) {

        List<String> definitions = dictionaryService.getDefinitions(language, word);

        System.out.println(definitions);
        DictionaryDto dto = DictionaryDto.builder()
                .definition(definitions)
                .language(language)
                .word(word)
                .build();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}

