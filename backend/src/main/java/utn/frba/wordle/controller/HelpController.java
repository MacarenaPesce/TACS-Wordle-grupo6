package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.HelpRequestDto;
import utn.frba.wordle.dto.HelpSolutionDto;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.service.HelpService;

import java.io.IOException;


@RestController
@RequestMapping("/api/help")
@CrossOrigin
public class HelpController {

    @Autowired
    HelpService helpService;

    @PostMapping("/{language}")
    public ResponseEntity<HelpSolutionDto> solution(@RequestBody HelpRequestDto helpRequestDto, @PathVariable Language language) throws IOException {

        //TODO verificar que los campos de entrada contengan solo letras
        //TODO el campo solution tambien tiene '_' y solo longitud 5

        HelpSolutionDto dto = helpService.solution(helpRequestDto, language);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
