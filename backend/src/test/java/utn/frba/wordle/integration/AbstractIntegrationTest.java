package utn.frba.wordle.integration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.enums.TournamentType;
import utn.frba.wordle.service.TournamentService;
import utn.frba.wordle.service.UserService;

import java.util.Calendar;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class AbstractIntegrationTest {

    @Autowired
    protected UserService userService;

    @Autowired
    protected TournamentService tournamentService;

    protected UserDto getUserDto(String email, String username) {
        return userService.createUser(username, "123asd", email);
    }


    protected TournamentDto getTournamentDto(UserDto owner, String tournamentName, State state, TournamentType type, Language language) {
        Date startDate;
        Date finishDate;

        switch (state){
            case READY:
                startDate = getTodayWithOffset(5);
                finishDate = getTodayWithOffset(10);
                break;
            case STARTED:
                startDate = getTodayWithOffset(-2);
                finishDate = getTodayWithOffset(5);
                break;
            case FINISHED:
                startDate = getTodayWithOffset(-10);
                finishDate = getTodayWithOffset(-3);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + state);
        }
        return getTournamentDto(owner, tournamentName, type, language, startDate, finishDate);
    }

    protected TournamentDto getTournamentDto(UserDto owner, String tournamentName, TournamentType type, Language language, Date startDate, Date finishDate) {
        TournamentDto tournamentDto = TournamentDto.builder()
                .type(type)
                .start(startDate)
                .finish(finishDate)
                .name(tournamentName)
                .language(language)
                .owner(owner)
                .build();
        return tournamentService.create(tournamentDto, owner.getId());
    }

    protected TournamentDto getTournamentDto(UserDto ownerUser, String tournamentName, Language language) {
        return getTournamentDto(ownerUser, tournamentName, State.STARTED, TournamentType.PRIVATE, language);
    }

    protected TournamentDto getTournamentDto(UserDto ownerUser) {
        return getTournamentDto(ownerUser, "Private Tourney", Language.ES);
    }

    protected TournamentDto getPublicTournamentDto(UserDto owner, String tournamentName, State state, Date startDate, Date finishDate) {
        return getTournamentDto(owner, tournamentName, TournamentType.PUBLIC, Language.ES, startDate, finishDate);
    }

    protected TournamentDto getPublicTournamentDto(UserDto owner, String tournamentName, State state) {
        return getTournamentDto(owner, tournamentName, state, TournamentType.PUBLIC, Language.ES);
    }

    protected Date getTodayWithOffset(int offset) {
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, offset); //same with c.add(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }
}
