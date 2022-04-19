package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.DictionaryDto;
import utn.frba.wordle.model.Language;

import java.util.Collections;

@Service
@NoArgsConstructor
public class DictionaryService {

    public DictionaryDto getDefinitions(Language language, String word) {
        return DictionaryDto.builder()
                .word("palabra")
                .language(language)
                .definition(Collections.singletonList("la definicion de palabra"))
                .build();
    }
}
