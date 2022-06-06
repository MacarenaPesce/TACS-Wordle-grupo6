package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import utn.frba.wordle.controller.TournamentController;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.exception.SessionJWTException;
import utn.frba.wordle.model.dto.RegistrationDto;
import utn.frba.wordle.model.dto.ResultDto;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.entity.PunctuationEntity;
import utn.frba.wordle.model.entity.TournamentEntity;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.enums.TournamentType;
import utn.frba.wordle.model.http.SubmitResultRequest;
import utn.frba.wordle.model.pojo.Punctuation;
import utn.frba.wordle.model.pojo.Session;
import utn.frba.wordle.repository.TournamentRepository;
import utn.frba.wordle.service.PunctuationService;
import utn.frba.wordle.service.RegistrationService;
import utn.frba.wordle.utils.TestUtils;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class TournamentServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    PunctuationService punctuationService;

    @Test
    public void aUserCanCreateATournament(){
        String name = "TestTournament";
        UserDto owner = getUserDto("email@email.com", "usernameTest");
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
        String name = "TestTournament";
        Long nonExistentId = 22L;
        TournamentDto dto = TournamentDto.builder()
                .type(TournamentType.PRIVATE)
                .start(new Date())
                .finish(new Date())
                .state(State.STARTED)
                .name(name)
                .language(Language.ES)
                .build();

        assertThrows(SessionJWTException.class, () -> tournamentService.create(dto, nonExistentId));
    }

    @Test
    public void aUserCantAddAnotherUserToANonexistentTournament(){
        String userOwnerUsername = "usernameTest";
        String userOwnerEmail = "email@email.com";
        UserDto userOwner = getUserDto(userOwnerEmail, userOwnerUsername);
        String userUsername = "usernameTest2";
        String userEmail = "email@email.com2";
        userService.createUser(userUsername, "pass", userEmail);
        Long userId = 2L;

        assertThrows(BusinessException.class, () -> tournamentService.addMember(userId, 32167L, userOwner.getId()));
    }
    @Test
    public void aUserCantAddAnotherUserToATournamentThatDontOwn(){
        String name = "TestTournament";
        UserDto ownerUser = getUserDto("email@email.com", "usernameTest");
        UserDto magicUser = getUserDto("email@email.com2", "usernameTest2");
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
        String name = "TestTournament";
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
        TournamentDto tournamentDto = getPublicTournamentDto(owner, "Public Tourney", State.READY);

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
        TournamentDto tournamentReady = getPublicTournamentDto(owner, "Tournament1", State.READY);
        tournamentReady.setState(State.READY);
        TournamentEntity tournament1entity = tournamentService.mapToEntity(tournamentReady);
        tournamentRepository.save(tournament1entity);
        TournamentDto tournamentStarted = getPublicTournamentDto(owner, "Tournament2", State.STARTED);
        tournamentStarted.setState(State.STARTED);
        TournamentEntity tournament2entity = tournamentService.mapToEntity(tournamentStarted);
        tournamentRepository.save(tournament2entity);
        TournamentDto tournamentFinished = getPublicTournamentDto(owner, "Tournament3", State.FINISHED);
        tournamentFinished.setState(State.FINISHED);
        TournamentEntity tournament3entity = tournamentService.mapToEntity(tournamentFinished);
        tournamentRepository.save(tournament3entity);

        List<TournamentDto> tournaments = tournamentService.listPublicActiveTournaments();

        assertThat(tournaments).containsExactlyInAnyOrder(tournamentReady, tournamentStarted);
    }

    @Test
    public void aUserCanFindByTournamentName() {
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        TournamentDto tournamentReady = getPublicTournamentDto(owner, "Alpha", State.READY);
        tournamentReady.setState(State.READY);
        TournamentEntity tournament1entity = tournamentService.mapToEntity(tournamentReady);
        tournamentRepository.save(tournament1entity);
        TournamentDto tournamentAlphabet = getPublicTournamentDto(owner, "Alphabet", State.READY);
        tournamentAlphabet.setState(State.READY);
        TournamentEntity tournamentAentity = tournamentService.mapToEntity(tournamentAlphabet);
        tournamentRepository.save(tournamentAentity);
        TournamentDto tournamentStarted = getPublicTournamentDto(owner, "Beta", State.READY);
        tournamentStarted.setState(State.STARTED);
        TournamentEntity tournament2entity = tournamentService.mapToEntity(tournamentStarted);
        tournamentRepository.save(tournament2entity);
        TournamentDto tournamentFinished = getPublicTournamentDto(owner, "Gamma", State.READY);
        tournamentFinished.setState(State.FINISHED);
        TournamentEntity tournament3entity = tournamentService.mapToEntity(tournamentFinished);
        tournamentRepository.save(tournament3entity);

        List<TournamentDto> tournaments = tournamentService.findPublicActiveTournaments("alph");

        assertThat(tournaments).containsExactlyInAnyOrder(tournamentReady, tournamentAlphabet);
    }

    @Test
    public void aUserCanSubmitTheirResults() {
        UserDto user = getUserDto("mail2@mail.com", "usernameTest2");
        UserDto player = getUserDto("mailPlayer@mail.com", "usernamePlayer");
        ResultDto resultDto = ResultDto.builder()
                .result(2L)
                .language(Language.ES)
                .build();
        TournamentDto tournamentDto = getPublicTournamentDto(user, "T23", State.STARTED);
        tournamentService.addMember(player.getId(), tournamentDto.getTourneyId(), user.getId());

        tournamentService.submitResults(player.getId(), resultDto);

        List<RegistrationDto> registrations =  registrationService.getRegistrationsFromUser(player.getId());
        List<PunctuationEntity> punctuations = punctuationService.getPunctuationsEntityFromTourney(registrations.get(0).getTournamentId());
        assertThat(punctuations).isNotEmpty();
        assertThat(punctuations.get(0)).hasNoNullFieldsOrProperties();
    }

    @Test
    public void aUserCantSubmitResultsGreaterThan8() {
        Session session = TestUtils.getMockSession();

        SubmitResultRequest result = SubmitResultRequest.builder()
                .result(8L)
                .language(Language.ES)
                .build();

        assertThrows(BusinessException.class, () -> new TournamentController().submitResults(session.getToken(), result));
    }

    @Test
    public void aUserCantSubmitResultsLesserThan1() {
        Session session = TestUtils.getMockSession();

        SubmitResultRequest result = SubmitResultRequest.builder()
                .result(0L)
                .language(Language.ES)
                .build();

        assertThrows(BusinessException.class, () -> new TournamentController().submitResults(session.getToken(), result));
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
    public void aUserCanSeeTheirScoreFromATournament(){
        UserDto player1 = getUserDto("mail1@mail.com", "player1");
        UserDto player2 = getUserDto("mail2@mail.com", "player2");
        UserDto player3 = getUserDto("mail3@mail.com", "player3");
        UserDto player4 = getUserDto("mail4@mail.com", "player4");
        TournamentDto tournamentDto = getPublicTournamentDto(player1, "Public Tourney", State.STARTED);
        tournamentService.addMember(player2.getId(), tournamentDto.getTourneyId(), player1.getId());
        tournamentService.addMember(player3.getId(), tournamentDto.getTourneyId(), player1.getId());
        tournamentService.addMember(player4.getId(), tournamentDto.getTourneyId(), player1.getId());
        ResultDto result = ResultDto.builder().result(5L).language(Language.ES).build();
        ResultDto result2 = ResultDto.builder().result(2L).language(Language.ES).build();
        ResultDto result3 = ResultDto.builder().result(3L).language(Language.ES).build();
        tournamentService.submitResults(player1.getId(), result);
        tournamentService.submitResults(player2.getId(), result2);
        tournamentService.submitResults(player3.getId(), result3);

        Punctuation scorePlayer1 = tournamentService.getScoreFromUser(tournamentDto.getTourneyId(), player1.getUsername());
        Punctuation scorePlayer2 = tournamentService.getScoreFromUser(tournamentDto.getTourneyId(), player2.getUsername());
        Punctuation scorePlayer3 = tournamentService.getScoreFromUser(tournamentDto.getTourneyId(), player3.getUsername());
        Punctuation scorePlayer4 = tournamentService.getScoreFromUser(tournamentDto.getTourneyId(), player4.getUsername());

        assertEquals(scorePlayer1.getPunctuation(), 19L);
        assertEquals(scorePlayer1.getPosition(), 4L);
        assertEquals(scorePlayer2.getPunctuation(), 16L);
        assertEquals(scorePlayer2.getPosition(), 2L);
        assertEquals(scorePlayer3.getPunctuation(), 17L);
        assertEquals(scorePlayer3.getPosition(), 3L);
        assertEquals(scorePlayer4.getPunctuation(), 14L);
        assertEquals(scorePlayer4.getPosition(), 1L);
    }

    @Test
    public void ifATournamentStartedTwoDaysAgoAndEndedYesterdayMyScoreShouldBeFourteen(){
        UserDto player1 = getUserDto("mail1@mail.com", "player1");
        Date startDate = getTodayWithOffset(-2);
        Date finishDate = getTodayWithOffset(-1);
        TournamentDto tournamentDto = getPublicTournamentDto(player1, "Public Tourney", State.READY, startDate, finishDate);

        List<Punctuation> punctuations = tournamentService.getRanking(tournamentDto.getTourneyId());

        assertThat(punctuations).isNotEmpty();
        assertThat(punctuations.get(0)).isNotNull();
        assertEquals(1L, punctuations.get(0).getPosition());
        assertEquals(14L, punctuations.get(0).getPunctuation());
    }

    @Test
    public void ifATournamentStartsAndEndsTomorrowMyScoreShouldBeZero(){
        UserDto player1 = getUserDto("mail1@mail.com", "player1");
        Date startDate = getTodayWithOffset(1);
        Date finishDate = getTodayWithOffset(1);
        TournamentDto tournamentDto = getPublicTournamentDto(player1, "Public Tourney", State.READY, startDate, finishDate);

        List<Punctuation> punctuations = tournamentService.getRanking(tournamentDto.getTourneyId());

        assertThat(punctuations).isNotEmpty();
        assertThat(punctuations.get(0)).isNotNull();
        assertEquals(1L, punctuations.get(0).getPosition());
        assertEquals(0L, punctuations.get(0).getPunctuation());
    }

    @Test
    public void ifATournamentStartsInTheFutureMyScoreShouldBeZero(){
        UserDto player1 = getUserDto("mail1@mail.com", "player1");
        TournamentDto tournamentDto = getPublicTournamentDto(player1, "Public Tourney", State.READY);
        List<Punctuation> punctuations = tournamentService.getRanking(tournamentDto.getTourneyId());

        assertThat(punctuations).isNotEmpty();
        assertThat(punctuations.get(0)).isNotNull();
        assertEquals(1L, punctuations.get(0).getPosition());
        assertEquals(0L, punctuations.get(0).getPunctuation());
    }

    @Test
    public void aUserCanSeeThePositionsTableOfATournament(){
        UserDto player1 = getUserDto("mail1@mail.com", "player1");
        UserDto player2 = getUserDto("mail2@mail.com", "player2");
        UserDto player3 = getUserDto("mail3@mail.com", "player3");
        UserDto player4 = getUserDto("mail4@mail.com", "player4");
        TournamentDto tournamentDto = getPublicTournamentDto(player1, "Public Tourney", State.STARTED);
        tournamentService.addMember(player2.getId(), tournamentDto.getTourneyId(), player1.getId());
        tournamentService.addMember(player3.getId(), tournamentDto.getTourneyId(), player1.getId());
        tournamentService.addMember(player4.getId(), tournamentDto.getTourneyId(), player1.getId());
        ResultDto result = ResultDto.builder().result(5L).language(Language.ES).build();
        ResultDto result2 = ResultDto.builder().result(2L).language(Language.ES).build();
        ResultDto result3 = ResultDto.builder().result(3L).language(Language.ES).build();
        tournamentService.submitResults(player1.getId(), result);
        tournamentService.submitResults(player2.getId(), result2);
        tournamentService.submitResults(player3.getId(), result3);

        List<Punctuation> punctuations = tournamentService.getRanking(tournamentDto.getTourneyId());

        assertThat(punctuations).isNotEmpty();
        assertThat(punctuations.get(0)).isNotNull();
        assertTrue(punctuations.get(0).getPunctuation() < punctuations.get(1).getPunctuation());
        assertTrue(punctuations.get(1).getPunctuation() < punctuations.get(2).getPunctuation());
        assertTrue(punctuations.get(2).getPunctuation() < punctuations.get(3).getPunctuation());
        assertEquals(1L, punctuations.get(0).getPosition());
        assertEquals(2L, punctuations.get(1).getPosition());
        assertEquals(3L, punctuations.get(2).getPosition());
        assertEquals(4L, punctuations.get(3).getPosition());
        assertEquals(14L, punctuations.get(0).getPunctuation());
        assertEquals(16L, punctuations.get(1).getPunctuation());
        assertEquals(17L, punctuations.get(2).getPunctuation());
        assertEquals(19L, punctuations.get(3).getPunctuation());
        assertFalse(punctuations.get(0).getSubmittedScoreToday());
        assertTrue(punctuations.get(1).getSubmittedScoreToday());
        assertTrue(punctuations.get(2).getSubmittedScoreToday());
        assertTrue(punctuations.get(3).getSubmittedScoreToday());
    }

    @Test
    public void anActiveTournamentWithDuplicatedNameCantBeCreated() {
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        getPublicTournamentDto(owner, "Tournament1", State.READY);

        assertThrows(BusinessException.class, () -> getPublicTournamentDto(owner, "Tournament1", State.READY));
    }

    @Test
    public void anActiveTournamentWithDuplicatedNameCanBeCreatedIfTheDuplicatedIsNotActive() {
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        TournamentDto tournament1 = getPublicTournamentDto(owner, "Tournament1", State.FINISHED);
        TournamentDto tournament2 = getPublicTournamentDto(owner, "Tournament1", State.READY);

        assertNotEquals(tournament1.getTourneyId(), tournament2.getTourneyId());
    }

    @Test
    @Transactional
    public void aUserCanListTheirFinishedTournaments(){
        State state = State.FINISHED;
        UserDto owner = getUserDto("owner@mail.com", "owner");
        UserDto player1 = getUserDto("player1@mail.com", "player1");
        UserDto player2 = getUserDto("player2@mail.com", "player2");
        TournamentDto tournament1 = getPublicTournamentDto(owner, "Tournament1", State.FINISHED);
        tournament1.setState(state);
        TournamentEntity tournament1entity = tournamentService.mapToEntity(tournament1);
        tournamentRepository.save(tournament1entity);
        tournamentService.addMember(player1.getId(), tournament1.getTourneyId(), owner.getId());
        tournamentService.addMember(player2.getId(), tournament1.getTourneyId(), owner.getId());
        tournamentRepository.findById(tournament1.getTourneyId()).orElseThrow();
        TournamentDto tournament2 = getPublicTournamentDto(owner, "Tournament2", State.STARTED);
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
        TournamentDto tournamentReady = getPublicTournamentDto(owner, "Tournament1", State.READY);
        tournamentReady.setState(State.READY);
        TournamentEntity tournament1entity = tournamentService.mapToEntity(tournamentReady);
        tournamentRepository.save(tournament1entity);
        tournamentService.addMember(player1.getId(), tournamentReady.getTourneyId(), owner.getId());
        TournamentDto tournamentStarted = getPublicTournamentDto(owner, "Tournament2", State.STARTED);
        tournamentStarted.setState(State.STARTED);
        TournamentEntity tournament2entity = tournamentService.mapToEntity(tournamentStarted);
        tournamentRepository.save(tournament2entity);
        tournamentService.addMember(player1.getId(), tournamentStarted.getTourneyId(), owner.getId());
        TournamentDto tournamentFinished = getPublicTournamentDto(owner, "Tournament3", State.FINISHED);
        tournamentFinished.setState(State.FINISHED);
        TournamentEntity tournament3entity = tournamentService.mapToEntity(tournamentFinished);
        tournamentRepository.save(tournament3entity);
        tournamentService.addMember(player1.getId(), tournamentFinished.getTourneyId(), owner.getId());

        List<TournamentDto> tournaments = tournamentService.getActiveTournamentsFromUser(player1.getId());

        assertThat(tournaments).isNotEmpty();
        assertEquals(tournaments.size(), 2);
        tournaments.forEach(tournamentDto -> assertThat(tournamentDto).hasNoNullFieldsOrProperties());
        assertThat(tournaments).containsExactlyInAnyOrder(tournamentReady, tournamentStarted);
    }

    @Test
    @Transactional
    public void aUserCanFilterTheirTournaments(){
        UserDto owner = getUserDto("owner@mail.com", "owner");
        UserDto player1 = getUserDto("player1@mail.com", "player1");
        TournamentDto tournamentReady = getPublicTournamentDto(owner, "T12", State.READY);
        tournamentReady.setState(State.READY);
        TournamentEntity tournament1entity = tournamentService.mapToEntity(tournamentReady);
        tournamentRepository.save(tournament1entity);
        tournamentService.addMember(player1.getId(), tournamentReady.getTourneyId(), owner.getId());
        TournamentDto tournamentStarted = getPublicTournamentDto(owner, "Tournament2", State.STARTED);
        tournamentStarted.setState(State.STARTED);
        TournamentEntity tournament2entity = tournamentService.mapToEntity(tournamentStarted);
        tournamentRepository.save(tournament2entity);
        tournamentService.addMember(player1.getId(), tournamentStarted.getTourneyId(), owner.getId());
        TournamentDto tournamentFinished = getPublicTournamentDto(owner, "T23", State.FINISHED);
        tournamentFinished.setState(State.FINISHED);
        TournamentEntity tournament3entity = tournamentService.mapToEntity(tournamentFinished);
        tournamentRepository.save(tournament3entity);
        tournamentService.addMember(player1.getId(), tournamentFinished.getTourneyId(), owner.getId());

        List<TournamentDto> tournaments = tournamentService.findActiveTournamentsFromUser(player1.getId(), "T2");

        assertThat(tournaments).isNotEmpty();
        assertEquals(tournaments.size(), 1);
        tournaments.forEach(tournamentDto -> assertThat(tournamentDto).hasNoNullFieldsOrProperties());
        assertThat(tournaments).containsExactlyInAnyOrder(tournamentStarted);
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
        String tournamentName = "TestTournament";
        return getPrivateTournamentDto(ownerUser, tournamentName);
    }
}

