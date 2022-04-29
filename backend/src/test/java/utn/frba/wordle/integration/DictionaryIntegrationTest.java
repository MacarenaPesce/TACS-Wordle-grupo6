package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.dto.DictionaryDto;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.service.DictionaryService;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class DictionaryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    DictionaryService service;

    @Test
    public void aUserCanGetTheDefinitionOfSpanishWord(){
        String word = "Caja";

        DictionaryDto dto = service.getDefinitions(Language.ES, word);

        assertThat(dto).hasNoNullFieldsOrProperties();
    }

    @Test
    public void aUserCanGetTheDefinitionOfEnglishWord(){
        String word = "Box";

        DictionaryDto dto = service.getDefinitions(Language.EN, word);

        assertThat(dto).hasNoNullFieldsOrProperties();
    }
}

