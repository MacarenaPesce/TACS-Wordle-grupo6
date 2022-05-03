package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.dto.HelpRequestDto;
import utn.frba.wordle.dto.HelpSolutionDto;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.service.HelpService;

import java.io.IOException;


public class HelpIntegrationTest extends AbstractIntegrationTest{

    @Autowired
    HelpService helpService;


    @Test
    public void canFindWordsWith() throws IOException {
        HelpRequestDto helpRequest = HelpRequestDto.builder()
                .yellow("aeis")
                .grey("")
                .solution("")
                .build();

        HelpSolutionDto helpSolution = helpService.solution(helpRequest, Language.ES);

        assertThat(helpSolution).hasNoNullFieldsOrProperties();
        assertThat(helpSolution.getPossibleWords()).contains("ilesa", "sepia");
    }

    @Test
    public void canFindWordsWithout() throws IOException {
        HelpRequestDto helpRequest = HelpRequestDto.builder()
                .yellow("")
                .grey("oie")
                .solution("_____")
                .build();

        HelpSolutionDto helpSolution = helpService.solution(helpRequest, Language.EN);

        assertThat(helpSolution).hasNoNullFieldsOrProperties();
        assertThat(helpSolution.getPossibleWords()).doesNotContain("voice", "oxide");
    }

    @Test
    public void canFindWordsMatching() throws IOException {
        HelpRequestDto helpRequest = HelpRequestDto.builder()
                .yellow("")
                .grey("")
                .solution("a_a_a")
                .build();

        HelpSolutionDto helpSolution = helpService.solution(helpRequest, Language.ES);

        assertThat(helpSolution).hasNoNullFieldsOrProperties();
        assertThat(helpSolution.getPossibleWords()).contains("asada", "amada");
    }

    @Test
    public void canFindWords() throws IOException {
        HelpRequestDto helpRequest = HelpRequestDto.builder()
                .yellow("ui")
                .grey("edkn")
                .solution("o____")
                .build();

        HelpSolutionDto helpSolution = helpService.solution(helpRequest, Language.EN);

        assertThat(helpSolution).hasNoNullFieldsOrProperties();
        assertThat(helpSolution.getPossibleWords()).contains("opium", "ouija");
    }

    @Test
    public void canNormalize() throws IOException {
        HelpRequestDto helpRequest = HelpRequestDto.builder()
                .yellow("!!!!!DAFF#@#@#----\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDC7Daaaaaaaaaaaaaa")
                .grey("oOoOOOooo¡°¢∈í́́íííí́E¢√∞×∞∈×∧ℚθωγrrrrℝℝℝρℝrℝ")
                .solution("_u___∴༼ つ ◕‿◕ ༽つ\uD83E\uDD2A")
                .build();

        HelpSolutionDto helpSolution = helpService.solution(helpRequest, Language.ES);

        assertThat(helpSolution).hasNoNullFieldsOrProperties();
        assertThat(helpSolution.getPossibleWords()).contains("funda");
    }

    @Test
    public void tooLongWord() throws IOException {
        HelpRequestDto helpRequest = HelpRequestDto.builder()
                .yellow("am")
                .grey("gr")
                .solution("o____largo")
                .build();

        assertThrows(BusinessException.class, () -> helpService.solution(helpRequest, Language.EN));
    }

}
