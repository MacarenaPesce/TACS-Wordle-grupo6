package utn.frba.wordle.integration;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.model.dto.ResultDto;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.pojo.Punctuation;
import utn.frba.wordle.service.PunctuationService;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


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

    @Test
    public void aUserCantPublishTheirPunctuationOnFinishedTournament() {
        UserDto user = getUserDto("email@email.com", "usernameTest");
        Date startDate = getTodayWithOffset(-2);
        Date finishDate = getTodayWithOffset(-1);
        TournamentDto tournamentDto = getPublicTournamentDto(user, "Public Tourney", State.FINISHED, startDate, finishDate);
        ResultDto resultESDto = ResultDto.builder()
                .language(Language.ES)
                .result(2L)
                .userId(user.getId())
                .build();

        punctuationService.submitResults(user.getId(), resultESDto);

        List<Punctuation> punctuations = tournamentService.getRanking(tournamentDto.getTourneyId(), 1, 100);
        assertThat(punctuations).isNotEmpty();
        AssertionsForClassTypes.assertThat(punctuations.get(0)).isNotNull();
        assertEquals(1L, punctuations.get(0).getPosition());
        assertEquals(14L, punctuations.get(0).getPunctuation());
    }

    @Test
    public void aUserCantPublishTheirPunctuationOnNotStartedTournament() {
        UserDto user = getUserDto("email@email.com", "usernameTest");
        Date startDate = getTodayWithOffset(1);
        Date finishDate = getTodayWithOffset(2);
        TournamentDto tournamentDto = getPublicTournamentDto(user, "Public Tourney", State.READY, startDate, finishDate);
        ResultDto resultESDto = ResultDto.builder()
                .language(Language.ES)
                .result(2L)
                .userId(user.getId())
                .build();

        punctuationService.submitResults(user.getId(), resultESDto);

        List<Punctuation> punctuations = tournamentService.getRanking(tournamentDto.getTourneyId(), 1, 100);
        assertThat(punctuations).isNotEmpty();
        AssertionsForClassTypes.assertThat(punctuations.get(0)).isNotNull();
        assertEquals(1L, punctuations.get(0).getPosition());
        assertEquals(0L, punctuations.get(0).getPunctuation());
    }
}
