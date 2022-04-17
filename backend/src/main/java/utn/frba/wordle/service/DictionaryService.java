package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.DictionaryDto;

import java.util.Collections;

@Service
@NoArgsConstructor
public class DictionaryService {

    public DictionaryDto getDefinitions(String word) {
        return DictionaryDto.builder()
                .word("palabra")
                .language("ES")
                .definition(Collections.singletonList("la definicion de palabra"))
                .build();
    }
}
