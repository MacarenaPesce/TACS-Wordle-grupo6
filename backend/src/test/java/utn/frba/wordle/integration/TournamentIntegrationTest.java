package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.model.TournamentType;
import utn.frba.wordle.repository.TournamentRepository;
import utn.frba.wordle.service.TournamentService;
import utn.frba.wordle.service.UserService;
import utn.frba.wordle.utils.TestUtils;

import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TournamentIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    TournamentService tournamentService;

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    UserService userService;

    @Test
    public void aUserCanCreateATournament(){
        String name = "TorneoPrueba";
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        TournamentDto dto = TournamentDto.builder()
                .type(TournamentType.PRIVATE)
                .start(new Date())
                .finish(new Date())
                .name(name)
                .language(Language.ES)
                .build();

        dto = tournamentService.create(dto, owner.getId());

        assertThat(dto).hasNoNullFieldsOrProperties();
        assertEquals(dto.getName(), name);
    }

    @Test
    public void aUserCantAddAnotherUserToANonexistentTournament(){
        UserDto userOwner = getUserDto("mail@mail.com", "usernameTest");
        LoginDto user = LoginDto.builder()
                .email("mail@mail.com2")
                .username("usernameTest2")
                .build();
        userService.createUser(user);
        MemberDto newMember = MemberDto.builder()
                .username("Richard")
                .tournamentId(32167L)
                .build();

        assertThrows(BusinessException.class, () -> tournamentService.addMember(newMember, userOwner.getId()));

    }
    @Test
    public void aUserCantAddAnotherUserToATournamentThatDontOwn(){
        String name = "TorneoPrueba";
        UserDto ownerUser = getUserDto("mail@mail.com", "usernameTest");
        UserDto magicUser = getUserDto("mail@mail.com2", "usernameTest2");
        MemberDto newMember = MemberDto.builder()
                .username("Richard")
                .tournamentId(32167L)
                .build();
        TournamentDto dto = TournamentDto.builder()
                .type(TournamentType.PRIVATE)
                .start(new Date())
                .finish(new Date())
                .name(name)
                .language(Language.ES)
                .owner(magicUser)
                .build();
        tournamentService.create(dto, ownerUser.getId());

        assertThrows(BusinessException.class, () -> tournamentService.addMember(newMember, ownerUser.getId()));

    }

    @Test
    public void aUserCanAddAnotherUserToATournamentThatOwns(){
        String name = "TorneoPrueba";
        UserDto ownerUser = getUserDto("mail@mail.com", "usernameTest");
        TournamentDto dto = TournamentDto.builder()
                .type(TournamentType.PRIVATE)
                .start(new Date())
                .finish(new Date())
                .name(name)
                .language(Language.ES)
                .owner(ownerUser)
                .build();
        dto = tournamentService.create(dto, ownerUser.getId());
        UserDto player = getUserDto("mail@mail.com2", "usernameTest2");
        MemberDto newMember = MemberDto.builder()
                .username(player.getUsername())
                .tournamentId(dto.getTourneyId())
                .build();

        tournamentService.addMember(newMember, ownerUser.getId());

        Set<UserDto> members = userService.getTournamentMembers(dto.getTourneyId());
        assertThat(members).contains(player);
    }

    @Test
    public void aUserCantBeAddedToATournamentTwice(){
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        TournamentDto tournamentDto = getPrivateTournamentDto(owner);
        UserDto user = getUserDto("mail2@mail.com", "usernameTest2");

        tournamentService.join(user.getId(), tournamentDto.getTourneyId());

        assertThrows(BusinessException.class, () -> tournamentService.join(user.getId(), tournamentDto.getTourneyId()));
    }

    @Test
    public void aUserCanJoinAPublicTournament() {
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        TournamentDto tournamentDto = getPrivateTournamentDto(owner);
        UserDto user = getUserDto("mail2@mail.com", "usernameTest2");
        //SessionDto sessionDto = TestUtils.getValidSessionFromUser(user);

        tournamentService.join(user.getId(), tournamentDto.getTourneyId());

        Set<UserDto> members = userService.getTournamentMembers(tournamentDto.getTourneyId());
        assertThat(members).contains(user);
    }

    @Test
    public void aUserCanListAllPublicTournaments() {
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        TournamentDto tournament1 = getPublicTournamentDto(owner, "Tournament1");
        TournamentDto tournament2 = getPublicTournamentDto(owner, "Tournament2");

        TourneysDto tournaments = tournamentService.listPublicTournaments();

        assertThat(tournaments.getTourneys()).containsExactlyInAnyOrder(tournament1, tournament2);
    }


    @Test
    public void aUserCanSubmitHisResult() {
        UserDto user = getUserDto("mail2@mail.com", "usernameTest2");
        ResultDto resultDto = ResultDto.builder()
                .result(2L)
                .language(Language.ES)
                .build();

        resultDto = tournamentService.submitResults(user.getId(), resultDto);

        assertThat(resultDto).hasNoNullFieldsOrProperties();
    }

    private UserDto getUserDto(String s, String usernameTest2) {
        LoginDto user = LoginDto.builder()
                .email(s)
                .username(usernameTest2)
                .build();
        return userService.createUser(user);
    }

    private TournamentDto getPublicTournamentDto(UserDto ownerUser, String tournamentName) {
        TournamentDto tournamentDto = TournamentDto.builder()
                .type(TournamentType.PUBLIC)
                .start(new Date())
                .finish(new Date())
                .name(tournamentName)
                .language(Language.ES)
                .owner(ownerUser)
                .build();
        return tournamentService.create(tournamentDto, ownerUser.getId());
    }

    private TournamentDto getPrivateTournamentDto(UserDto ownerUser, String tournamentName) {
        TournamentDto tournamentDto = TournamentDto.builder()
                .type(TournamentType.PRIVATE)
                .start(new Date())
                .finish(new Date())
                .name(tournamentName)
                .language(Language.ES)
                .owner(ownerUser)
                .build();
        return tournamentService.create(tournamentDto, ownerUser.getId());
    }

    private TournamentDto getPrivateTournamentDto(UserDto ownerUser) {
        String tournamentName = "TorneoPrueba";
        return getPrivateTournamentDto(ownerUser, tournamentName);
    }
}

