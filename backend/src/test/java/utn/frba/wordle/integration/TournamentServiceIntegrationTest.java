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
import utn.frba.wordle.model.http.FindTournamentsFilters;
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
        TournamentDto tournamentDto = getPublicTournamentDto(owner, "T1", State.STARTED);
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
        TournamentDto tournamentDto = getPublicTournamentDto(owner, "T1", State.READY);
        UserDto user = getUserDto("mail2@mail.com", "usernameTest2");
        //SessionDto sessionDto = TestUtils.getValidSessionFromUser(user);

        tournamentService.join(user.getId(), tournamentDto.getTourneyId());

        List<UserDto> members = userService.getTournamentMembers(tournamentDto.getTourneyId());
        assertThat(members).contains(user);
    }

    @Test
    public void aUserCanListAllPublicTournaments() {
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        TournamentDto tournamentReady = savePublicTournament(owner, "Tournament1", State.READY);
        TournamentDto tournamentStarted = savePublicTournament(owner, "Tournament2", State.STARTED);
        savePublicTournament(owner, "Tournament3", State.FINISHED);

        List<TournamentDto> tournaments = tournamentService.listPublicActiveTournaments(1, 100);

        assertThat(tournaments).containsExactlyInAnyOrder(tournamentReady, tournamentStarted);
    }

    @Test
    public void aUserCanListAllPublicTournamentsWithPagination() {
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        savePublicTournament(owner, "Tournament1", State.FINISHED);
        savePublicTournament(owner, "Tournament1", State.READY);
        savePublicTournament(owner, "Tournament2", State.STARTED);
        TournamentDto tournamentStarted3 = savePublicTournament(owner, "Tournament3", State.STARTED);
        savePublicTournament(owner, "Tournament4", State.FINISHED);

        List<TournamentDto> tournaments = tournamentService.listPublicActiveTournaments(2, 2);

        assertThat(tournaments).containsExactlyInAnyOrder(tournamentStarted3);
    }

    @Test
    public void aUserCanFindByTournamentName() {
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        TournamentDto tournamentReady = savePublicTournament(owner, "Alpha", State.READY);
        TournamentDto tournamentAlphabet = savePublicTournament(owner, "Alphabet", State.READY);
        savePublicTournament(owner, "Beta", State.READY);
        savePublicTournament(owner, "Gamma", State.READY);

        List<TournamentDto> tournaments = tournamentService.findPublicActiveTournaments("alph", 1, 100);

        assertThat(tournaments).containsExactlyInAnyOrder(tournamentReady, tournamentAlphabet);
    }

    @Test
    public void aUserCanFindByTournamentNameWithMultipleFilters() {
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        TournamentDto tournamentReady = savePublicTournament(owner, "Alpha", State.READY);
        savePrivateTournament(owner, "Alpha2", State.READY);
        savePrivateTournament(owner, "alPhi$", State.STARTED);
        TournamentDto tournamentAlphabet = savePublicTournament(owner, "Alphabet", State.STARTED);
        TournamentDto tournamentAlphis = savePublicTournament(owner, "alPhi$$", State.READY);
        savePublicTournament(owner, "Beta", State.READY);
        savePublicTournament(owner, "Gamma", State.READY);
        FindTournamentsFilters filters = FindTournamentsFilters.builder()
                .name("alph")
                .type(TournamentType.PUBLIC)
                .maxResults(100)
                .pageNumber(1)
                .build();

        List<TournamentDto> tournaments = tournamentService.findTournaments(filters);

        assertThat(tournaments).containsExactlyInAnyOrder(tournamentReady, tournamentAlphabet, tournamentAlphis);
    }

    @Test
    public void aUserCanFindByTournamentNameWithPagination() {
        UserDto owner = getUserDto("mail@mail.com", "usernameTest");
        savePublicTournament(owner, "Alpha", State.READY);
        savePublicTournament(owner, "Alphabet", State.READY);
        TournamentDto tournamentAlphys = savePublicTournament(owner, "Alphys Tournament", State.READY);
        savePublicTournament(owner, "Beta", State.READY);
        savePublicTournament(owner, "Gamma", State.READY);

        List<TournamentDto> tournaments = tournamentService.findPublicActiveTournaments("alph", 2, 2);

        assertThat(tournaments).containsExactlyInAnyOrder(tournamentAlphys);
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
        submitResult(player, 2L, Language.ES);

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
        TournamentDto tournamentDto = getTournamentDto(user);
        tournamentService.addMember(player.getId(), tournamentDto.getTourneyId(), user.getId());
        tournamentService.submitResults(player.getId(), resultDto);

        assertThrows(BusinessException.class, () -> tournamentService.submitResults(player.getId(), resultDto));
    }


    @Test
    public void aUserCanGetTheResultsOfToday(){
        UserDto user = getUserDto("mail2@mail.com", "usernameTest2");
        UserDto player = getUserDto("mailPlayer@mail.com", "usernamePlayer");
        TournamentDto tournamentDtoSpanish = getTournamentDto(user, "Private Tourney ES", Language.ES);
        tournamentService.addMember(player.getId(), tournamentDtoSpanish.getTourneyId(), user.getId());
        submitResult(player, 3L, Language.ES);
        TournamentDto tournamentDtoEnglish = getTournamentDto(user, "Private Tourney EN", Language.EN);
        tournamentService.addMember(player.getId(), tournamentDtoEnglish.getTourneyId(), user.getId());
        submitResult(player, 5L, Language.EN);


        Long punctuationEnglish = punctuationService.getTodaysResult(player.getId(), Language.EN);
        Long punctuationSpanish = punctuationService.getTodaysResult(player.getId(), Language.ES);

        assertEquals(punctuationSpanish, 3L);
        assertEquals(punctuationEnglish, 5L);
    }

    @Test
    public void aUserCanGetTheResultsOfTodayWithNoPunctuations(){
        UserDto user = getUserDto("mail2@mail.com", "usernameTest2");
        UserDto player = getUserDto("mailPlayer@mail.com", "usernamePlayer");
        TournamentDto tournamentDtoSpanish = getTournamentDto(user, "Private Tourney ES", Language.ES);
        tournamentService.addMember(player.getId(), tournamentDtoSpanish.getTourneyId(), user.getId());
        TournamentDto tournamentDtoEnglish = getTournamentDto(user, "Private Tourney EN", Language.EN);
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
        TournamentDto tournamentDto = getPublicTournamentDto(player1, "My Public Tourney", State.FINISHED, startDate, finishDate);

        List<Punctuation> punctuations = tournamentService.getRanking(tournamentDto.getTourneyId(), 1, 100);

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

        List<Punctuation> punctuations = tournamentService.getRanking(tournamentDto.getTourneyId(), 1, 100);

        assertThat(punctuations).isNotEmpty();
        assertThat(punctuations.get(0)).isNotNull();
        assertEquals(1L, punctuations.get(0).getPosition());
        assertEquals(0L, punctuations.get(0).getPunctuation());
    }

    @Test
    public void ifATournamentStartsAndEndsInTheFutureTheScoreShouldBeZeroForAllParticipants(){
        UserDto player1 = getUserDto("mail1@mail.com", "player1");
        UserDto player2 = getUserDto("mail1@mail2.com", "player2");
        UserDto player3 = getUserDto("mail1@mail3.com", "player3");
        UserDto player4 = getUserDto("mail1@mail4.com", "player4");
        Date startDate = getTodayWithOffset(3);
        Date finishDate = getTodayWithOffset(10);
        getPublicTournamentDto(player1, "Tournament1", State.READY);
        getPublicTournamentDto(player1, "Tournament2", State.STARTED);
        getPublicTournamentDto(player1, "Tournament3", State.STARTED);
        getPublicTournamentDto(player1, "Tournament4", State.FINISHED);
        TournamentDto futureTournament = getPublicTournamentDto(player1, "Public Tourney", State.READY, startDate, finishDate);
        tournamentService.addMember(player2.getId(), futureTournament.getTourneyId(), player1.getId());
        tournamentService.addMember(player3.getId(), futureTournament.getTourneyId(), player1.getId());
        tournamentService.addMember(player4.getId(), futureTournament.getTourneyId(), player1.getId());

        List<Punctuation> punctuations = tournamentService.getRanking(futureTournament.getTourneyId(), 1, 100);

        assertThat(punctuations).isNotEmpty();
        assertThat(punctuations.get(0)).isNotNull();
        assertThat(punctuations.get(1)).isNotNull();
        assertThat(punctuations.get(2)).isNotNull();
        assertThat(punctuations.get(3)).isNotNull();
        assertEquals(1L, punctuations.get(0).getPosition());
        assertEquals(0L, punctuations.get(0).getPunctuation());
        assertEquals(2L, punctuations.get(1).getPosition());
        assertEquals(0L, punctuations.get(1).getPunctuation());
        assertEquals(3L, punctuations.get(2).getPosition());
        assertEquals(0L, punctuations.get(2).getPunctuation());
        assertEquals(4L, punctuations.get(3).getPosition());
        assertEquals(0L, punctuations.get(3).getPunctuation());
    }

    @Test
    public void aUserCanGetRankingWithPagination(){
        UserDto player1 = getUserDto("mail1@mail.com", "player1");
        UserDto player2 = getUserDto("mail1@mail2.com", "player2");
        UserDto player3 = getUserDto("mail1@mail3.com", "player3");
        UserDto player4 = getUserDto("mail1@mail4.com", "player4");
        Date startDate = getTodayWithOffset(0);
        Date finishDate = getTodayWithOffset(3);
        TournamentDto futureTournament = getPublicTournamentDto(player1, "Public Tourney", State.STARTED, startDate, finishDate);
        tournamentService.addMember(player2.getId(), futureTournament.getTourneyId(), player1.getId());
        tournamentService.addMember(player3.getId(), futureTournament.getTourneyId(), player1.getId());
        tournamentService.addMember(player4.getId(), futureTournament.getTourneyId(), player1.getId());
        submitResult(player1, 5L, Language.ES);
        submitResult(player2, 4L, Language.ES);
        submitResult(player3, 3L, Language.ES);
        submitResult(player4, 2L, Language.ES);

        List<Punctuation> punctuations = tournamentService.getRanking(futureTournament.getTourneyId(), 2, 2);

        assertEquals(punctuations.size(), 2);
        assertThat(punctuations.get(0)).isNotNull();
        assertThat(punctuations.get(1)).isNotNull();
        assertEquals(3L, punctuations.get(0).getPosition());
        assertEquals(4L, punctuations.get(0).getPunctuation());
        assertEquals(4L, punctuations.get(1).getPosition());
        assertEquals(5L, punctuations.get(1).getPunctuation());
    }

    @Test
    public void ifATournamentStartsInTheFutureMyScoreShouldBeZero(){
        UserDto player1 = getUserDto("mail1@mail.com", "player1");
        TournamentDto tournamentDto = getPublicTournamentDto(player1, "Public Tourney", State.READY);
        List<Punctuation> punctuations = tournamentService.getRanking(tournamentDto.getTourneyId(), 1, 100);

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
        submitResult(player1, 5L, Language.ES);
        submitResult(player2, 2L, Language.ES);
        submitResult(player3, 3L, Language.ES);

        List<Punctuation> punctuations = tournamentService.getRanking(tournamentDto.getTourneyId(), 1, 100);

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

    private void submitResult(UserDto player, long score, Language language) {
        ResultDto result = ResultDto.builder().result(score).language(language).build();
        tournamentService.submitResults(player.getId(), result);
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
        UserDto owner = getUserDto("owner@mail.com", "owner");
        UserDto player1 = getUserDto("player1@mail.com", "player1");
        UserDto player2 = getUserDto("player2@mail.com", "player2");
        TournamentDto tournament1 = savePublicTournament(owner, "Tournament1", State.FINISHED);
        tournamentService.addMember(player1.getId(), tournament1.getTourneyId(), owner.getId());
        tournamentService.addMember(player2.getId(), tournament1.getTourneyId(), owner.getId());
        TournamentDto tournament2 = savePublicTournament(owner, "Tournament2", State.STARTED);
        tournamentService.addMember(player2.getId(), tournament2.getTourneyId(), owner.getId());
        TournamentDto tournament3 = savePublicTournament(owner, "Tournament3", State.FINISHED);
        tournamentService.addMember(player2.getId(), tournament3.getTourneyId(), owner.getId());
        Integer actualPage = 1;
        Integer maxResults = 100;

        List<TournamentDto> tournaments = tournamentService.findUserTournamentsByStateWithPagination(player2.getId(), State.FINISHED, actualPage, maxResults);

        assertThat(tournaments).isNotEmpty();
        assertEquals(2, tournaments.size());
        assertThat(tournaments.get(0)).hasNoNullFieldsOrProperties();
        assertEquals(tournaments.get(0).getState(), State.FINISHED);
    }

    @Test
    @Transactional
    public void aUserCanListTheirStartedTournaments(){
        UserDto owner = getUserDto("owner@mail.com", "owner");
        UserDto player1 = getUserDto("player1@mail.com", "player1");
        UserDto player2 = getUserDto("player2@mail.com", "player2");
        TournamentDto tournament1 = savePublicTournament(owner, "Tournament1", State.FINISHED);
        tournamentService.addMember(player1.getId(), tournament1.getTourneyId(), owner.getId());
        tournamentService.addMember(player2.getId(), tournament1.getTourneyId(), owner.getId());
        TournamentDto tournament2 = savePublicTournament(owner, "Tournament2", State.STARTED);
        tournamentService.addMember(player2.getId(), tournament2.getTourneyId(), owner.getId());
        Integer actualPage = 1;
        Integer maxResults = 100;

        List<TournamentDto> tournaments = tournamentService.findUserTournamentsByStateWithPagination(player2.getId(), State.STARTED, actualPage, maxResults);

        assertThat(tournaments).isNotEmpty();
        assertEquals(1, tournaments.size());
        assertThat(tournaments.get(0)).hasNoNullFieldsOrProperties();
        assertEquals(tournaments.get(0).getState(), State.STARTED);
    }

    @Test
    @Transactional
    public void aUserCanListTheirReadyTournaments(){
        UserDto owner = getUserDto("owner@mail.com", "owner");
        UserDto player1 = getUserDto("player1@mail.com", "player1");
        UserDto player2 = getUserDto("player2@mail.com", "player2");
        TournamentDto tournament1 = savePublicTournament(owner, "Tournament1", State.FINISHED);
        tournamentService.addMember(player1.getId(), tournament1.getTourneyId(), owner.getId());
        tournamentService.addMember(player2.getId(), tournament1.getTourneyId(), owner.getId());
        TournamentDto tournament2 = savePublicTournament(owner, "Tournament2", State.STARTED);
        tournamentService.addMember(player2.getId(), tournament2.getTourneyId(), owner.getId());
        TournamentDto tournament3 = savePublicTournament(owner, "Tournament3", State.FINISHED);
        tournamentService.addMember(player2.getId(), tournament3.getTourneyId(), owner.getId());
        TournamentDto tournament4 = savePublicTournament(owner, "Tournament4", State.READY);
        tournamentService.addMember(player1.getId(), tournament4.getTourneyId(), owner.getId());
        tournamentService.addMember(player2.getId(), tournament4.getTourneyId(), owner.getId());
        TournamentDto tournament5 = savePublicTournament(owner, "Tournament5", State.READY);
        tournamentService.addMember(player2.getId(), tournament5.getTourneyId(), owner.getId());
        TournamentDto tournament6 = savePublicTournament(owner, "Tournament6", State.READY);
        tournamentService.addMember(player2.getId(), tournament6.getTourneyId(), owner.getId());
        Integer actualPage = 1;
        Integer maxResults = 100;

        List<TournamentDto> tournaments = tournamentService.findUserTournamentsByStateWithPagination(player2.getId(), State.READY, actualPage, maxResults);

        assertThat(tournaments).isNotEmpty();
        assertEquals(3, tournaments.size());
        assertThat(tournaments.get(0)).hasNoNullFieldsOrProperties();
        assertEquals(tournaments.get(0).getState(), State.READY);
    }

    @Test
    @Transactional
    public void aUserCanListTheirReadyTournamentsWithPagination(){
        UserDto owner = getUserDto("owner@mail.com", "owner");
        UserDto player1 = getUserDto("player1@mail.com", "player1");
        UserDto player2 = getUserDto("player2@mail.com", "player2");
        TournamentDto tournament1 = savePublicTournament(owner, "Tournament1", State.READY);
        tournamentService.addMember(player1.getId(), tournament1.getTourneyId(), owner.getId());
        tournamentService.addMember(player2.getId(), tournament1.getTourneyId(), owner.getId());
        TournamentDto tournament2 = savePublicTournament(owner, "Tournament2", State.READY);
        tournamentService.addMember(player2.getId(), tournament2.getTourneyId(), owner.getId());
        TournamentDto tournament3 = savePublicTournament(owner, "Tournament3", State.READY);
        tournamentService.addMember(player2.getId(), tournament3.getTourneyId(), owner.getId());
        TournamentDto tournament4 = savePublicTournament(owner, "Tournament4", State.READY);
        tournamentService.addMember(player1.getId(), tournament4.getTourneyId(), owner.getId());
        tournamentService.addMember(player2.getId(), tournament4.getTourneyId(), owner.getId());
        TournamentDto tournament5 = savePublicTournament(owner, "Tournament5", State.READY);
        tournamentService.addMember(player2.getId(), tournament5.getTourneyId(), owner.getId());
        TournamentDto tournament6 = savePublicTournament(owner, "Tournament6", State.READY);
        tournamentService.addMember(player2.getId(), tournament6.getTourneyId(), owner.getId());
        Integer actualPage = 2;
        Integer maxResults = 4;

        List<TournamentDto> tournaments = tournamentService.findUserTournamentsByStateWithPagination(player2.getId(), State.READY, actualPage, maxResults);

        assertThat(tournaments).isNotEmpty();
        assertEquals(2, tournaments.size());
        assertThat(tournaments.get(0)).hasNoNullFieldsOrProperties();
        assertEquals(tournaments.get(0).getState(), State.READY);
    }

    @Test
    @Transactional
    public void aUserCanListTheirStartedTournamentsWithPagination(){
        UserDto owner = getUserDto("owner@mail.com", "owner");
        UserDto player1 = getUserDto("player1@mail.com", "player1");
        UserDto player2 = getUserDto("player2@mail.com", "player2");
        TournamentDto tournament1 = savePublicTournament(owner, "Tournament1", State.STARTED);
        tournamentService.addMember(player1.getId(), tournament1.getTourneyId(), owner.getId());
        tournamentService.addMember(player2.getId(), tournament1.getTourneyId(), owner.getId());
        TournamentDto tournament2 = savePublicTournament(owner, "Tournament2", State.STARTED);
        tournamentService.addMember(player2.getId(), tournament2.getTourneyId(), owner.getId());
        TournamentDto tournament3 = savePublicTournament(owner, "Tournament3", State.STARTED);
        tournamentService.addMember(player2.getId(), tournament3.getTourneyId(), owner.getId());
        TournamentDto tournament4 = savePublicTournament(owner, "Tournament4", State.STARTED);
        tournamentService.addMember(player1.getId(), tournament4.getTourneyId(), owner.getId());
        tournamentService.addMember(player2.getId(), tournament4.getTourneyId(), owner.getId());
        TournamentDto tournament5 = savePublicTournament(owner, "Tournament5", State.STARTED);
        tournamentService.addMember(player2.getId(), tournament5.getTourneyId(), owner.getId());
        Integer actualPage = 2;
        Integer maxResults = 4;

        List<TournamentDto> tournaments = tournamentService.findUserTournamentsByStateWithPagination(player2.getId(), State.STARTED, actualPage, maxResults);

        assertThat(tournaments).isNotEmpty();
        assertEquals(1, tournaments.size());
        assertThat(tournaments.get(0)).hasNoNullFieldsOrProperties();
        assertEquals(tournaments.get(0).getState(), State.STARTED);
    }

    @Test
    @Transactional
    public void aUserCanListTheirFinishedTournamentsWithPagination(){
        UserDto owner = getUserDto("owner@mail.com", "owner");
        UserDto player1 = getUserDto("player1@mail.com", "player1");
        UserDto player2 = getUserDto("player2@mail.com", "player2");
        TournamentDto tournament1 = savePublicTournament(owner, "Tournament1", State.FINISHED);
        tournamentService.addMember(player1.getId(), tournament1.getTourneyId(), owner.getId());
        tournamentService.addMember(player2.getId(), tournament1.getTourneyId(), owner.getId());
        TournamentDto tournament2 = savePublicTournament(owner, "Tournament2", State.FINISHED);
        tournamentService.addMember(player2.getId(), tournament2.getTourneyId(), owner.getId());
        TournamentDto tournament3 = savePublicTournament(owner, "Tournament3", State.FINISHED);
        tournamentService.addMember(player2.getId(), tournament3.getTourneyId(), owner.getId());
        TournamentDto tournament4 = savePublicTournament(owner, "Tournament4", State.FINISHED);
        tournamentService.addMember(player1.getId(), tournament4.getTourneyId(), owner.getId());
        tournamentService.addMember(player2.getId(), tournament4.getTourneyId(), owner.getId());
        TournamentDto tournament5 = savePublicTournament(owner, "Tournament5", State.FINISHED);
        tournamentService.addMember(player2.getId(), tournament5.getTourneyId(), owner.getId());
        Integer actualPage = 2;
        Integer maxResults = 3;

        List<TournamentDto> tournaments = tournamentService.findUserTournamentsByStateWithPagination(player2.getId(), State.FINISHED, actualPage, maxResults);

        assertThat(tournaments).isNotEmpty();
        assertEquals(2, tournaments.size());
        assertThat(tournaments.get(0)).hasNoNullFieldsOrProperties();
        assertEquals(tournaments.get(0).getState(), State.FINISHED);
    }

    @Test
    @Transactional
    public void aUserCanListAllTheirTournaments(){
        UserDto owner = getUserDto("owner@mail.com", "owner");
        UserDto player1 = getUserDto("player1@mail.com", "player1");
        TournamentDto tournamentReady = savePublicTournament(owner, "Tournament1", State.READY);
        tournamentService.addMember(player1.getId(), tournamentReady.getTourneyId(), owner.getId());
        TournamentDto tournamentStarted = savePublicTournament(owner, "Tournament2", State.STARTED);
        tournamentService.addMember(player1.getId(), tournamentStarted.getTourneyId(), owner.getId());
        TournamentDto tournamentFinished = savePublicTournament(owner, "Tournament3", State.FINISHED);
        tournamentService.addMember(player1.getId(), tournamentFinished.getTourneyId(), owner.getId());

        List<TournamentDto> tournaments = tournamentService.getActiveTournamentsFromUser(player1.getId(), 1, 20);

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
        TournamentDto tournamentReady = savePublicTournament(owner, "T12", State.READY);
        tournamentService.addMember(player1.getId(), tournamentReady.getTourneyId(), owner.getId());
        TournamentDto tournamentStarted = savePublicTournament(owner, "Tournament2", State.STARTED);
        tournamentService.addMember(player1.getId(), tournamentStarted.getTourneyId(), owner.getId());
        TournamentDto tournamentFinished = savePublicTournament(owner, "T23", State.FINISHED);
        tournamentService.addMember(player1.getId(), tournamentFinished.getTourneyId(), owner.getId());

        List<TournamentDto> tournaments = tournamentService.findActiveTournamentsFromUser(player1.getId(), "T2", 1, 20);

        assertThat(tournaments).isNotEmpty();
        assertEquals(tournaments.size(), 1);
        tournaments.forEach(tournamentDto -> assertThat(tournamentDto).hasNoNullFieldsOrProperties());
        assertThat(tournaments).containsExactlyInAnyOrder(tournamentStarted);
    }

    private TournamentDto savePrivateTournament(UserDto owner, String tournamentName, State state) {
        TournamentDto tournamentDto =  getTournamentDto(owner, tournamentName, state, TournamentType.PRIVATE, Language.ES);
        TournamentEntity tournamentEntity = tournamentService.mapToEntity(tournamentDto);
        tournamentRepository.save(tournamentEntity);
        return tournamentDto;
    }

    private TournamentDto savePublicTournament(UserDto owner, String tournamentName, State state) {
        TournamentDto tournamentDto = getTournamentDto(owner, tournamentName, state, TournamentType.PUBLIC, Language.ES);
        TournamentEntity tournamentEntity = tournamentService.mapToEntity(tournamentDto);
        tournamentRepository.save(tournamentEntity);
        return tournamentDto;
    }
}

