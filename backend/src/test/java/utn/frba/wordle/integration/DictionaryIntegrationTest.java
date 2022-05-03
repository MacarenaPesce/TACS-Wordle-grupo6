package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.dto.DictionaryDto;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.service.DictionaryService;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class DictionaryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    DictionaryService service;

    @Test
    public void aUserCanGetTheDefinitionOfSpanishWord(){
        String word = "Caja";
        Language lang = Language.ES;

        List<String> definitions = service.getDefinitions(lang, word);

        DictionaryDto dto = DictionaryDto.builder()
                .definition(definitions)
                .language(lang)
                .word(word)
                .build();

        assertThat(dto).hasNoNullFieldsOrProperties();
    }

    @Test
    public void aUserCanGetTheDefinitionOfEnglishWord(){
        String word = "Box";
        Language lang = Language.EN;

        List<String> definitions = service.getDefinitions(lang, word);

        DictionaryDto dto = DictionaryDto.builder()
                .definition(definitions)
                .language(lang)
                .word(word)
                .build();

        assertThat(dto).hasNoNullFieldsOrProperties();
    }
}

