package utn.frba.wordle.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TournamentServiceTest {

    @InjectMocks
    private TournamentService tournamentService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @SneakyThrows
    @Test
    public void parsesFinishDateCorrectly(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = sdf.parse("2022-06-21 00:00:00");
        Date recoveredDate = tournamentService.parseFinishDate(date);
        assertThat(recoveredDate).isNotNull();
        assertEquals(recoveredDate.toString(), "Tue Jun 21 20:59:59 ART 2022");
    }

}
