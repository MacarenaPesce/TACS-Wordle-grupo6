package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.HelpRequestDto;
import utn.frba.wordle.dto.HelpSolutionDto;
import utn.frba.wordle.service.HelpService;


@RestController
@RequestMapping("/api/help")
@CrossOrigin
public class HelpController {

    @Autowired
    HelpService helpService;

    @PostMapping("/{language}")
    public ResponseEntity<HelpSolutionDto> solution(@RequestBody HelpRequestDto helpRequestDto, @PathVariable String language) {

        HelpSolutionDto dto = helpService.solution(helpRequestDto, language);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
