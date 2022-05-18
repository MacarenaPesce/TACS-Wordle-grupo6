package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.controller.HelpController;
import utn.frba.wordle.model.dto.HelpRequestDto;
import utn.frba.wordle.model.dto.HelpSolutionDto;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.pojo.Language;
import utn.frba.wordle.service.HelpService;

import java.io.IOException;
import java.util.Set;


public class HelpIntegrationTest extends AbstractIntegrationTest{

    @Autowired
    HelpService helpService;

    //TODO acá en que parte son test de integración, si llaman solo al service?
    //al controller no lo llaman, si no que copian al controller con su propio código de manejo de DTOs

    public HelpSolutionDto buildSolutionDto(String yellow, String grey, String solution, Language lang) throws IOException {

        Set<String> helpSolution = helpService.solution(yellow, grey, solution, lang);

        return HelpSolutionDto.builder()
                .possibleWords(helpSolution)
                .build();
    }

    @Test
    public void canFindWordsWith() throws IOException {
        HelpRequestDto helpRequest = HelpRequestDto.builder()
                .yellow("aeis")
                .grey("")
                .solution("")
                .build();

        HelpSolutionDto responseDto = buildSolutionDto(helpRequest.getYellow(), helpRequest.getGrey(), helpRequest.getSolution(), Language.ES);

        assertThat(responseDto).hasNoNullFieldsOrProperties();
        assertThat(responseDto.getPossibleWords()).contains("ilesa", "sepia");
    }

    @Test
    public void canFindWordsWithout() throws IOException {
        HelpRequestDto helpRequest = HelpRequestDto.builder()
                .yellow("")
                .grey("oie")
                .solution("_____")
                .build();

        HelpSolutionDto helpSolution = buildSolutionDto(helpRequest.getYellow(), helpRequest.getGrey(), helpRequest.getSolution(), Language.EN);

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

        HelpSolutionDto helpSolution = buildSolutionDto(helpRequest.getYellow(), helpRequest.getGrey(), helpRequest.getSolution(), Language.ES);

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

        HelpSolutionDto helpSolution = buildSolutionDto(helpRequest.getYellow(), helpRequest.getGrey(), helpRequest.getSolution(), Language.EN);

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


        HelpRequestDto normalized = new HelpController().normalizeInput(helpRequest); //TODO por que repetir el codigo del controller, no puedo llamar directo al controller?
        HelpSolutionDto helpSolution = buildSolutionDto(normalized.getYellow(), normalized.getGrey(), normalized.getSolution(), Language.ES);

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

        assertThrows(BusinessException.class, () -> new HelpController().solution(helpRequest, Language.EN)); //TODO aca si puedo llamar directo al controller, ya que no necesito el dto respuesta
    }

}
