package utn.frba.wordle.integration;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import utn.frba.wordle.model.dto.*;
import utn.frba.wordle.model.entity.PunctuationEntity;
import utn.frba.wordle.model.entity.TournamentEntity;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.exception.SessionJWTException;
import utn.frba.wordle.model.pojo.Language;
import utn.frba.wordle.model.pojo.Ranking;
import utn.frba.wordle.model.pojo.State;
import utn.frba.wordle.model.pojo.TournamentType;
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
                .state(State.STARTED)
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
                .state(State.STARTED)
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

        List<UserDto> members = userService.getTournamentMembers(dto.getTourneyId());
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

        List<UserDto> members = userService.getTournamentMembers(tournamentDto.getTourneyId());
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
    public void aUserCanSubmitTheirResults() {
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
        List<PunctuationEntity> punctuations = punctuationService.getPunctuationsEntityFromTourney(registrations.get(0).getTournamentId());
        assertThat(punctuations).isNotEmpty();
        assertThat(punctuations.get(0)).hasNoNullFieldsOrProperties();
    }

    @Test
    public void aUserCanSubmitTheirResultsWithoutAnyRegistration() {
        UserDto player = getUserDto("mailPlayer@mail.com", "usernamePlayer");
        ResultDto resultDto = ResultDto.builder()
                .result(2L)
                .language(Language.ES)
                .build();

        tournamentService.submitResults(player.getId(), resultDto);

        Long punctuation = punctuationService.getTodaysResult(player.getId(), Language.ES);
        assertEquals(punctuation, 2L);
    }

    @Test
    public void aUserCantSubmitTheirResultsTwice() {
        UserDto user = getUserDto("mail2@mail.com", "usernameTest2");
        UserDto player = getUserDto("mailPlayer@mail.com", "usernamePlayer");
        ResultDto resultDto = ResultDto.builder()
                .result(2L)
                .language(Language.ES)
                .build();
        TournamentDto tournamentDto = getPrivateTournamentDto(user, "Private Tourney");
        tournamentService.addMember(player.getId(), tournamentDto.getTourneyId(), user.getId());
        tournamentService.submitResults(player.getId(), resultDto);

        assertThrows(BusinessException.class, () -> tournamentService.submitResults(player.getId(), resultDto));
    }


    @Test
    public void aUserCanGetTheResultsOfToday(){
        UserDto user = getUserDto("mail2@mail.com", "usernameTest2");
        UserDto player = getUserDto("mailPlayer@mail.com", "usernamePlayer");
        TournamentDto tournamentDtoSpanish = getPrivateTournamentDto(user, "Private Tourney ES", Language.ES);
        tournamentService.addMember(player.getId(), tournamentDtoSpanish.getTourneyId(), user.getId());
        ResultDto resultDtoSpanish = ResultDto.builder()
                .result(3L)
                .language(Language.ES)
                .build();
        tournamentService.submitResults(player.getId(), resultDtoSpanish);
        TournamentDto tournamentDtoEnglish = getPrivateTournamentDto(user, "Private Tourney EN", Language.EN);
        tournamentService.addMember(player.getId(), tournamentDtoEnglish.getTourneyId(), user.getId());
        ResultDto resultDtoEnglish = ResultDto.builder()
                .result(5L)
                .language(Language.EN)
                .build();
        tournamentService.submitResults(player.getId(), resultDtoEnglish);


        Long punctuationEnglish = punctuationService.getTodaysResult(player.getId(), Language.EN);
        Long punctuationSpanish = punctuationService.getTodaysResult(player.getId(), Language.ES);

        assertEquals(punctuationSpanish, 3L);
        assertEquals(punctuationEnglish, 5L);
    }

    @Test
    public void aUserCanGetTheResultsOfTodayWithNoPunctuations(){
        UserDto user = getUserDto("mail2@mail.com", "usernameTest2");
        UserDto player = getUserDto("mailPlayer@mail.com", "usernamePlayer");
        TournamentDto tournamentDtoSpanish = getPrivateTournamentDto(user, "Private Tourney ES", Language.ES);
        tournamentService.addMember(player.getId(), tournamentDtoSpanish.getTourneyId(), user.getId());
        TournamentDto tournamentDtoEnglish = getPrivateTournamentDto(user, "Private Tourney EN", Language.EN);
        tournamentService.addMember(player.getId(), tournamentDtoEnglish.getTourneyId(), user.getId());

        Long punctuationEnglish = punctuationService.getTodaysResult(player.getId(), Language.EN);
        Long punctuationSpanish = punctuationService.getTodaysResult(player.getId(), Language.ES);

        assertEquals(punctuationSpanish, 0L);
        assertEquals(punctuationEnglish, 0L);
    }

    @Test
    public void aUserCanGetTheResultsOfTodayWithNoTournaments(){
        UserDto player = getUserDto("mailPlayer@mail.com", "usernamePlayer");

        Long punctuationEnglish = punctuationService.getTodaysResult(player.getId(), Language.EN);
        Long punctuationSpanish = punctuationService.getTodaysResult(player.getId(), Language.ES);

        assertEquals(punctuationSpanish, 0L);
        assertEquals(punctuationEnglish, 0L);
    }

    @Test
    public void aUserCanSeeThePositionsTableOfATournament(){
        UserDto player1 = getUserDto("mail1@mail.com", "player1");
        UserDto player2 = getUserDto("mail2@mail.com", "player2");
        UserDto player3 = getUserDto("mail3@mail.com", "player3");
        TournamentDto tournamentDto = getPublicTournamentDto(player1, "Public Tourney");
        //tournamentService.addMember(player1.getId(), tournamentDto.getTourneyId(), player1.getId());
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
        inabilityTournament(tournament1, State.FINISHED);
        TournamentDto tournament2 = getPublicTournamentDto(owner, "Tournament1");

        assertNotEquals(tournament1.getTourneyId(), tournament2.getTourneyId());
    }

    @Test
    @Transactional
    public void aUserCanListTheirFinishedTournaments(){
        State state = State.FINISHED;
        UserDto owner = getUserDto("owner@mail.com", "owner");
        UserDto player1 = getUserDto("player1@mail.com", "player1");
        UserDto player2 = getUserDto("player2@mail.com", "player2");
        TournamentDto tournament1 = getPublicTournamentDto(owner, "Tournament1");
        tournament1.setState(state);
        TournamentEntity tournament1entity = tournamentService.mapToEntity(tournament1);
        tournamentRepository.save(tournament1entity);
        tournamentService.addMember(player1.getId(), tournament1.getTourneyId(), owner.getId());
        tournamentService.addMember(player2.getId(), tournament1.getTourneyId(), owner.getId());
        tournamentRepository.findById(tournament1.getTourneyId()).orElseThrow();
        TournamentDto tournament2 = getPublicTournamentDto(owner, "Tournament2");
        tournament2.setState(State.STARTED);
        TournamentEntity tournament2entity = tournamentService.mapToEntity(tournament2);
        tournamentRepository.save(tournament2entity);
        tournamentService.addMember(player2.getId(), tournament2.getTourneyId(), owner.getId());
        tournamentRepository.findById(tournament2.getTourneyId()).orElseThrow();

        List<TournamentDto> tournaments = tournamentService.findUserTournamentsByState(player2.getId(), state);

        assertThat(tournaments).isNotEmpty();
        assertEquals(tournaments.size(), 1);
        assertThat(tournaments.get(0)).hasNoNullFieldsOrProperties();
        assertEquals(tournaments.get(0).getState(), state);
    }

    @Test
    @Transactional
    public void aUserCanListAllTheirTournaments(){
        UserDto owner = getUserDto("owner@mail.com", "owner");
        UserDto player1 = getUserDto("player1@mail.com", "player1");
        TournamentDto tournament1 = getPublicTournamentDto(owner, "Tournament1");
        tournament1.setState(State.READY);
        TournamentEntity tournament1entity = tournamentService.mapToEntity(tournament1);
        tournamentRepository.save(tournament1entity);
        tournamentService.addMember(player1.getId(), tournament1.getTourneyId(), owner.getId());
        TournamentDto tournament2 = getPublicTournamentDto(owner, "Tournament2");
        tournament2.setState(State.STARTED);
        TournamentEntity tournament2entity = tournamentService.mapToEntity(tournament2);
        tournamentRepository.save(tournament2entity);
        tournamentService.addMember(player1.getId(), tournament2.getTourneyId(), owner.getId());
        TournamentDto tournament3 = getPublicTournamentDto(owner, "Tournament3");
        tournament3.setState(State.FINISHED);
        TournamentEntity tournament3entity = tournamentService.mapToEntity(tournament3);
        tournamentRepository.save(tournament3entity);
        tournamentService.addMember(player1.getId(), tournament3.getTourneyId(), owner.getId());

        List<TournamentDto> tournaments = tournamentService.getTournamentsFromUser(player1.getId());

        assertThat(tournaments).isNotEmpty();
        assertEquals(tournaments.size(), 3);
        tournaments.forEach(tournamentDto -> assertThat(tournamentDto).hasNoNullFieldsOrProperties());
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

    private TournamentDto getPrivateTournamentDto(UserDto ownerUser, String tournamentName, Language language) {
        TournamentDto tournamentDto = TournamentDto.builder()
                .type(TournamentType.PRIVATE)
                .start(new Date())
                .finish(new Date())
                .name(tournamentName)
                .language(language)
                .owner(ownerUser)
                .build();
        return tournamentService.create(tournamentDto, ownerUser.getId());
    }

    private TournamentDto getPrivateTournamentDto(UserDto ownerUser, String tournamentName) {
        return getPrivateTournamentDto(ownerUser, tournamentName, Language.ES);
    }

    private TournamentDto getPrivateTournamentDto(UserDto ownerUser) {
        String tournamentName = "TorneoPrueba";
        return getPrivateTournamentDto(ownerUser, tournamentName);
    }
}

