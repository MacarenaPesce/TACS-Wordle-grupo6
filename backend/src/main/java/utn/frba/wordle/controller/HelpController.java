package utn.frba.wordle.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.HelpRequestDto;
import utn.frba.wordle.dto.HelpSolutionDto;

import java.util.Arrays;


@RestController
@RequestMapping("/api/help")
@CrossOrigin
public class HelpController {

    @PostMapping("/{language}")
    public ResponseEntity<HelpSolutionDto> solution(@RequestBody HelpRequestDto helpRequestDto, @PathVariable String language) {
        HelpSolutionDto dto = HelpSolutionDto.builder()
                .possibleWords(Arrays.asList("ALLOW", "AGLOW", "APLOW", language, language, language, language))
                .build();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
