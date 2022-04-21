package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.DictionaryDto;
import utn.frba.wordle.service.DictionaryService;

import java.util.Collections;


@RestController
@RequestMapping("/api/dictionary")
@CrossOrigin
public class DictionaryController {

    @Autowired
    DictionaryService dictionaryService;

    @GetMapping("/{word}")
    public ResponseEntity<DictionaryDto> ask(@PathVariable String word) {

        DictionaryDto dto = dictionaryService.getDefinitions(word);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}

