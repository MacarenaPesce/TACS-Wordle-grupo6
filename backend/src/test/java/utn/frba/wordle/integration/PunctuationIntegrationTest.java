package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.dto.ResultDto;
import utn.frba.wordle.dto.UserDto;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.service.PunctuationService;


public class PunctuationIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    PunctuationService punctuationService;

    @Test
    public void aUserCanPublishTheirPunctuationWithoutATournament() {
        UserDto user = getUserDto("mail@mail.com", "usernameTest");

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
