package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.HelpDto;
import utn.frba.wordle.model.http.HelpRequest;
import utn.frba.wordle.model.http.HelpResponse;
import utn.frba.wordle.model.pojo.Language;
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
    public ResponseEntity<HelpResponse> solution(@RequestBody HelpRequest helpRequest, @PathVariable Language language) {

        HelpDto normalized = normalizeInput(helpRequest);

        Set<String> possibleSolutions = helpService.solution(normalized, language);

        HelpResponse responseDto = HelpResponse.builder()
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
    public HelpDto normalizeInput(HelpRequest helpRequestDto){
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

        return HelpDto.builder()
                .solution(solution)
                .grey(grey)
                .yellow(yellow)
                .build();
    }
}
