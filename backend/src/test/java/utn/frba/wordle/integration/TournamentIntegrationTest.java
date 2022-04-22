package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.dto.TournamentDto;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.model.TounamentType;
import utn.frba.wordle.service.TournamentService;

import java.util.Date;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TournamentIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    TournamentService tournamentService;

    @Test
    public void aUserCanCreateATournament(){
        String name = "TorneoPrueba";
        TournamentDto dto = TournamentDto.builder()
                .type(TounamentType.PRIVATE)
                .start(new Date())
                .finish(new Date())
                .name(name)
                .language(Language.ES)
                .build();

        dto = tournamentService.create(dto);

        assertThat(dto.getTourneyId()).isNotNull();
        assertEquals(dto.getName(), name);
    }
}

