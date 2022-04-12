package utn.frba.wordle.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.DictionaryDto;

import java.util.Collections;


@RestController
@RequestMapping("/api/dictionary")
@CrossOrigin
public class DictionaryController {

    @GetMapping("/{word}")
    public ResponseEntity<DictionaryDto> ask(@PathVariable String word) {
        DictionaryDto dto = DictionaryDto.builder()
                .word("palabra")
                .language("ES")
                .definition(Collections.singletonList("la definicion de palabra"))
                .build();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}

