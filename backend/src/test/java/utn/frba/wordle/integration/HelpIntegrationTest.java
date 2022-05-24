package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.controller.HelpController;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.HelpDto;
import utn.frba.wordle.model.http.HelpRequest;
import utn.frba.wordle.model.http.HelpResponse;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.service.HelpService;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HelpIntegrationTest extends AbstractIntegrationTest{

    @Autowired
    HelpService helpService;

    public HelpResponse buildSolutionDto(HelpDto helpDto, Language lang) {

        Set<String> helpSolution = helpService.solution(helpDto, lang);

        return HelpResponse.builder()
                .possibleWords(helpSolution)
                .build();
    }

    @Test
    public void canFindWordsWith() {
        HelpDto helpDto = HelpDto.builder()
                .yellow("aeis")
                .grey("")
                .solution("")
                .build();

        HelpResponse responseDto = buildSolutionDto(helpDto, Language.ES);

        assertThat(responseDto).hasNoNullFieldsOrProperties();
        assertThat(responseDto.getPossibleWords()).contains("ilesa", "sepia");
    }

    @Test
    public void canFindWordsWithout() {
        HelpDto helpDto = HelpDto.builder()
                .yellow("")
                .grey("oie")
                .solution("_____")
                .build();

        HelpResponse helpSolution = buildSolutionDto(helpDto, Language.EN);

        assertThat(helpSolution).hasNoNullFieldsOrProperties();
        assertThat(helpSolution.getPossibleWords()).doesNotContain("voice", "oxide");
    }

    @Test
    public void canFindWordsMatching() {
        HelpDto helpDto = HelpDto.builder()
                .yellow("")
                .grey("")
                .solution("a_a_a")
                .build();

        HelpResponse helpSolution = buildSolutionDto(helpDto, Language.ES);

        assertThat(helpSolution).hasNoNullFieldsOrProperties();
        assertThat(helpSolution.getPossibleWords()).contains("asada", "amada");
    }

    @Test
    public void canFindWords() {
        HelpDto helpDto = HelpDto.builder()
                .yellow("ui")
                .grey("edkn")
                .solution("o____")
                .build();

        HelpResponse helpSolution = buildSolutionDto(helpDto, Language.EN);

        assertThat(helpSolution).hasNoNullFieldsOrProperties();
        assertThat(helpSolution.getPossibleWords()).contains("opium", "ouija");
    }

    //TODO pasar esto a otro tipo de test, crear una nueva clase que solo normalice, o delegar en otro objeto
    @Test
    public void canNormalize() {
        HelpRequest helpRequest = HelpRequest.builder()
                .yellow("!!!!!DAFF#@#@#----\uD83D\uDE0E\uD83D\uDE0E\uD83D\uDC7Daaaaaaaaaaaaaa")
                .grey("oOoOOOooo¡°¢∈í́́íííí́E¢√∞×∞∈×∧ℚθωγrrrrℝℝℝρℝrℝ")
                .solution("_u___∴༼ つ ◕‿◕ ༽つ\uD83E\uDD2A")
                .build();


        HelpDto normalized = new HelpController().normalizeInput(helpRequest); //TODO por que repetir el codigo del controller, no puedo llamar directo al controller?
        HelpResponse helpSolution = buildSolutionDto(normalized, Language.ES);

        assertThat(helpSolution).hasNoNullFieldsOrProperties();
        assertThat(helpSolution.getPossibleWords()).contains("funda");
    }

    @Test
    public void tooLongWord() {
        HelpRequest helpRequest = HelpRequest.builder()
                .yellow("am")
                .grey("gr")
                .solution("o____largo")
                .build();

        assertThrows(BusinessException.class, () -> new HelpController().solution(helpRequest, Language.EN)); //TODO aca si puedo llamar directo al controller, ya que no necesito el dto respuesta
    }

}
