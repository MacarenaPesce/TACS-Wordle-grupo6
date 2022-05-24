package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.model.dto.ResultDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.service.PunctuationService;


public class PunctuationServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    PunctuationService punctuationService;

    @Test
    public void aUserCanPublishTheirPunctuationWithoutATournament() {
        UserDto user = getUserDto("email@email.com", "usernameTest");

        ResultDto resultESDto = ResultDto.builder()
                .language(Language.ES)
                .result(2L)
                .userId(user.getId())
                .build();

        ResultDto resultENDto = ResultDto.builder()
                .language(Language.EN)
                .result(2L)
                .userId(user.getId())
                .build();

        punctuationService.submitResults(user.getId(), resultESDto);
        punctuationService.submitResults(user.getId(), resultENDto);
    }
}
