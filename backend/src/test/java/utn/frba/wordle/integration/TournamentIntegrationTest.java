package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.entity.PunctuationEntity;
import utn.frba.wordle.entity.TournamentEntity;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.exception.SessionJWTException;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.model.Ranking;
import utn.frba.wordle.model.State;
import utn.frba.wordle.model.TournamentType;
import utn.frba.wordle.repository.TournamentRepository;
import utn.frba.wordle.service.PunctuationService;
import utn.frba.wordle.service.RegistrationService;
import utn.frba.wordle.service.TournamentService;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class TournamentIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    TournamentService tournamentService;

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    PunctuationService punctuationService;

    @Test
    public void aUserCanCreateATournament(){
        String name = "TorneoPrueba";
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        TournamentDto dto = TournamentDto.builder()
                .type(TournamentType.PRIVATE)
                .start(new Date())
                .finish(new Date())
                .state(State.ACTIVE)
                .name(name)
                .language(Language.ES)
                .build();

        dto = tournamentService.create(dto, owner.getId());

        assertThat(dto).hasNoNullFieldsOrProperties();
        assertEquals(dto.getName(), name);
    }

    @Test
    public void aUserTriesToCreateATournamentButHeDoesNotExist(){
        String name = "TorneoPrueba";
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        //(el owner tiene id 1)
        Long idInexistente = 22L;
        TournamentDto dto = TournamentDto.builder()
                .type(TournamentType.PRIVATE)
                .start(new Date())
                .finish(new Date())
                .state(State.ACTIVE)
                .name(name)
                .language(Language.ES)
                .build();

        assertThrows(SessionJWTException.class, () -> tournamentService.create(dto, idInexistente));
    }

    @Test
    public void aUserCantAddAnotherUserToANonexistentTournament(){
        UserDto userOwner = getUserDto("mail@mail.com", "usernameTest");
        LoginDto user = LoginDto.builder()
                .email("mail@mail.com2")
                .username("usernameTest2")
                .build();
        userService.createUser(user);
        Long userId = 2L;

        assertThrows(BusinessException.class, () -> tournamentService.addMember(userId, 32167L, userOwner.getId()));

    }
    @Test
    public void aUserCantAddAnotherUserToATournamentThatDontOwn(){
        String name = "TorneoPrueba";
        UserDto ownerUser = getUserDto("mail@mail.com", "usernameTest");
        UserDto magicUser = getUserDto("mail@mail.com2", "usernameTest2");
        Long userId = 2L;
        TournamentDto dto = TournamentDto.builder()
                .type(TournamentType.PRIVATE)
                .start(new Date())
                .finish(new Date())
                .name(name)
                .language(Language.ES)
                .owner(magicUser)
                .build();
        tournamentService.create(dto, ownerUser.getId());

        assertThrows(BusinessException.class, () -> tournamentService.addMember(userId, 32167L, ownerUser.getId()));

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
        Long userId = 2L;

        tournamentService.addMember(userId, dto.getTourneyId(), ownerUser.getId());

        Set<UserDto> members = userService.getTournamentMembers(dto.getTourneyId());
        assertThat(members).contains(player);
    }

    @Test
    public void aUserCantBeAddedToATournamentTwice(){
        UserDto owner = getUserDto("mail2@mail.com", "usernameTest2");
        UserDto user = getUserDto("mailPlayer@mail.com", "usernamePlayer");
        TournamentDto tournamentDto = getPublicTournamentDto(owner, "Public Tourney");

        tournamentService.join(user.getId(), tournamentDto.getTourneyId());

        assertThrows(BusinessException.class, () -> tournamentService.join(user.getId(), tournamentDto.getTourneyId()));
    }

    @Test
    public void aUserCantSubmitHisResultsOfTheSameLanguageTwiceInADay(){
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        TournamentDto tournamentDto = getPrivateTournamentDto(owner);
        UserDto user = getUserDto("mail2@mail.com", "usernameTest2");
        tournamentService.join(user.getId(), tournamentDto.getTourneyId());
        ResultDto dto = ResultDto.builder()
                .result(5L)
                .language(Language.ES)
                .build();

        tournamentService.submitResults(user.getId(), dto);

        assertThrows(BusinessException.class, () -> tournamentService.submitResults(user.getId(), dto));

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
        UserDto player = getUserDto("mailPlayer@mail.com", "usernamePlayer");
        ResultDto resultDto = ResultDto.builder()
                .result(2L)
                .language(Language.ES)
                .build();
        TournamentDto tournamentDto = getPrivateTournamentDto(user, "Private Tourney");
        tournamentService.addMember(player.getId(), tournamentDto.getTourneyId(), user.getId());

        tournamentService.submitResults(player.getId(), resultDto);

        List<RegistrationDto> registrations =  registrationService.getRegistrationsFromUser(player.getId());
        List<PunctuationEntity> punctuations = punctuationService.getPunctuationsEntityFromTourney(registrations.get(0).getId());
        assertThat(punctuations).isNotEmpty();
        assertThat(punctuations.get(0)).hasNoNullFieldsOrProperties();
    }

    @Test
    public void aUserCanSeeThePositionsTableOfATournament(){
        UserDto player1 = getUserDto("mail1@mail.com", "player1");
        UserDto player2 = getUserDto("mail2@mail.com", "player2");
        UserDto player3 = getUserDto("mail3@mail.com", "player3");
        TournamentDto tournamentDto = getPublicTournamentDto(player1, "Public Tourney");
        tournamentService.addMember(player1.getId(), tournamentDto.getTourneyId(), player1.getId());
        tournamentService.addMember(player2.getId(), tournamentDto.getTourneyId(), player1.getId());
        tournamentService.addMember(player3.getId(), tournamentDto.getTourneyId(), player1.getId());
        ResultDto result = ResultDto.builder().result(5L).language(Language.ES).build();
        ResultDto result2 = ResultDto.builder().result(2L).language(Language.ES).build();
        ResultDto result3 = ResultDto.builder().result(3L).language(Language.ES).build();
        tournamentService.submitResults(player1.getId(), result);
        tournamentService.submitResults(player2.getId(), result2);
        tournamentService.submitResults(player3.getId(), result3);

        Ranking ranking = tournamentService.getRanking(tournamentDto.getTourneyId());

        assertThat(ranking).hasNoNullFieldsOrProperties();
        assertThat(ranking.getPunctuations()).isNotEmpty();
        assertThat(ranking.getPunctuations().get(0)).isNotEqualTo(0);
        assertTrue(ranking.getPunctuations().get(0).getPunctuation() > ranking.getPunctuations().get(1).getPunctuation());
        assertTrue(ranking.getPunctuations().get(1).getPunctuation() > ranking.getPunctuations().get(2).getPunctuation());
    }

    @Test
    public void anActiveTournamentWithDuplicatedNameCantBeCreated() {
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        getPublicTournamentDto(owner, "Tournament1");

        assertThrows(BusinessException.class, () -> getPublicTournamentDto(owner, "Tournament1"));
    }

    @Test
    public void anActiveTournamentWithDuplicatedNameCanBeCreatedIfTheDuplicatedIsNotActive() {
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        TournamentDto tournament1 = getPublicTournamentDto(owner, "Tournament1");
        inabilityTournament(tournament1, State.INACTIVE);
        TournamentDto tournament2 = getPublicTournamentDto(owner, "Tournament1");

        assertNotEquals(tournament1.getTourneyId(), tournament2.getTourneyId());
    }

    private void inabilityTournament(TournamentDto tournament1, State state) {
        TournamentEntity entity = tournamentRepository.findById(tournament1.getTourneyId()).orElseThrow();
        entity.setState(state);

        tournamentRepository.save(entity);
    }

    private TournamentDto getPublicTournamentDto(UserDto owner, String tournamentName) {
        TournamentDto tournamentDto = TournamentDto.builder()
                .type(TournamentType.PUBLIC)
                .start(new Date())
                .finish(new Date())
                .name(tournamentName)
                .language(Language.ES)
                .owner(owner)
                .build();
        return tournamentService.create(tournamentDto, owner.getId());
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

