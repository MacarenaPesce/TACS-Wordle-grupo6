package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.HelpRequestDto;
import utn.frba.wordle.dto.HelpSolutionDto;

import java.util.Arrays;

@Service
@NoArgsConstructor
public class HelpService {

    public HelpSolutionDto solution(HelpRequestDto helpRequestDto, String language) {
        return HelpSolutionDto.builder()
                .possibleWords(Arrays.asList("ALLOW", "AGLOW", "APLOW", language, language, language, language))
                .build();
    }
}
