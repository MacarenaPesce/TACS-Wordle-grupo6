package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.HelpRequestDto;
import utn.frba.wordle.dto.HelpSolutionDto;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.service.HelpService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/help")
@CrossOrigin
public class HelpController {

    @Autowired
    HelpService helpService;

    @PostMapping("/{language}")
    public ResponseEntity<HelpSolutionDto> solution(@RequestBody HelpRequestDto helpRequestDto, @PathVariable Language language) throws IOException {

        HelpRequestDto normalized = normalizeInput(helpRequestDto);

        Set<String> possibleSolutions = helpService.solution(normalized.getYellow(), normalized.getGrey(), normalized.getSolution(), language);

        HelpSolutionDto responseDto = HelpSolutionDto.builder()
                            .possibleWords(possibleSolutions)
                            .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Normalizes the input data to ensure it's processed correctly in lowercase,
     * avoid processing the same letter multiple times, avoid hackers, etc...
     * @param helpRequestDto DTO with the data captured from the wild world wide web
     * @return now safe DTO
     */
    public HelpRequestDto normalizeInput(HelpRequestDto helpRequestDto){
        //remove non letters and make lowercase
        String yellow = helpRequestDto.getYellow().replaceAll("[^A-Za-z]+", "").toLowerCase();
        String grey = helpRequestDto.getGrey().replaceAll("[^A-Za-z]+", "").toLowerCase();
        String solution = helpRequestDto.getSolution().replaceAll("[^A-Za-z_]+", "").toLowerCase();

        if( !(solution.length() == 5 || solution.length() == 0)){
            throw new BusinessException("solution '"+solution+"' can not have a lenght of "+solution.length());
        }

        //remove duplicates
        yellow = Arrays.stream(yellow.split(""))
                .distinct()
                .collect(Collectors.joining());
        grey = Arrays.stream(grey.split(""))
                .distinct()
                .collect(Collectors.joining());

        helpRequestDto.setGrey(grey);
        helpRequestDto.setYellow(yellow);
        helpRequestDto.setSolution(solution);

        return helpRequestDto;
    }
}
