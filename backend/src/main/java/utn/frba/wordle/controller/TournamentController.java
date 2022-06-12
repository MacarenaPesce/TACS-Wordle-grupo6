package utn.frba.wordle.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.RegistrationDto;
import utn.frba.wordle.model.dto.ResultDto;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.http.*;
import utn.frba.wordle.model.pojo.Punctuation;
import utn.frba.wordle.model.pojo.Session;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.TournamentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tournaments")
@CrossOrigin
public class TournamentController {

    @Autowired
    TournamentService tournamentService;

    private static final Logger logger = LoggerFactory.getLogger(TournamentController.class);

    @PostMapping
    public ResponseEntity<TournamentResponse> create(@RequestHeader("Authorization") String token,
                                                     @RequestBody CreateTournamentRequest request) {
        logger.info("Method: create - Request: token={}, request={}", token, request);

        Session session = AuthService.getSession(token);
        TournamentDto newTournament = TournamentDto.builder()
                .language(request.getLanguage())
                .name(request.getName())
                .type(request.getType())
                .start(request.getStart())
                .finish(request.getFinish())
                .build();

        TournamentResponse response = buildResponse(tournamentService.create(newTournament, session.getUserId()));

        logger.info("Method: create - Response: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/myTournaments")
    public ResponseEntity<TournamentsListResponse> getActiveTournamentsFromUser(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer maxResults){
        logger.info("Method: getTournamentsFromUser - Request: token={}, pageNumber={}, maxResults={}", token, pageNumber, maxResults);

        Session session = AuthService.getSession(token);
        if(pageNumber == null || maxResults == null){
            pageNumber = 1;
            maxResults = 100;
        }

        Integer totalPages;
        List<TournamentDto> tournamentsDto;
        if(name == null) {
            tournamentsDto = tournamentService.getActiveTournamentsFromUser(session.getUserId(), pageNumber, maxResults);
            totalPages =  tournamentService.getActiveTournamentsFromUserTotalPages(session.getUserId(), maxResults);
        }
        else {
            tournamentsDto = tournamentService.findActiveTournamentsFromUser(session.getUserId(), name, pageNumber, maxResults);
            totalPages =  tournamentService.findActiveTournamentsFromUserTotalPages(session.getUserId(), name, maxResults);
        }

        List<TournamentResponse> tournaments = tournamentsDto
                .stream().map(this::buildResponse).collect(Collectors.toList());


        TournamentsListResponse response = TournamentsListResponse.builder()
                .tournaments(tournaments)
                .maxResults(maxResults)
                .pageNumber(pageNumber)
                .totalPages(totalPages)
                .build();

        logger.info("Method: getTournamentsFromUser - Response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("public")
    public ResponseEntity<TournamentsListResponse> listPublicActiveTournaments(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer maxResults){
        logger.info("Method: listPublicTournaments - Request: token={}, pageNumber={}, maxResults={}", token, pageNumber, maxResults);
        List<TournamentDto> tournamentsDto;

        if(pageNumber == null || maxResults == null){
            pageNumber = 1;
            maxResults = 100;
        }
        Integer totalPages;
        if(name == null) {
            tournamentsDto = tournamentService.listPublicActiveTournaments(pageNumber, maxResults);
            totalPages =  tournamentService.listPublicActiveTournamentsTotalPages(maxResults);
        }
        else {
            tournamentsDto = tournamentService.findPublicActiveTournaments(name, pageNumber, maxResults);
            totalPages =  tournamentService.findPublicActiveTournamentsTotalPages(name, maxResults);
        }

        List<TournamentResponse> tournaments = tournamentsDto
                .stream().map(this::buildResponse).collect(Collectors.toList());

        TournamentsListResponse response = TournamentsListResponse.builder()
                .tournaments(tournaments)
                .maxResults(maxResults)
                .pageNumber(pageNumber)
                .totalPages(totalPages)
                .build();

        logger.info("Method: listPublicTournaments - Response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/info/{tournamentId}")
    public ResponseEntity<TournamentResponse> getTournamentFromId(@RequestHeader("Authorization") String token,
                                                                  @PathVariable Long tournamentId) {
        logger.info("Method: getTournament - Request: token={}, tournamentId={}", token, tournamentId);

        TournamentDto tournamentDto = tournamentService.getTournamentFromId(tournamentId);

        TournamentResponse response = buildResponse(tournamentDto);

        logger.info("Method: getTournamentsFromUser - Response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{tournamentId}/members/{userId}")
    public ResponseEntity<RegistrationResponse> addMember(@RequestHeader("Authorization") String token,
                                                          @PathVariable Long userId,
                                                          @PathVariable Long tournamentId) {
        logger.info("Method: addMember - Request: token={}, userId={}, tournamentId={}", token, userId, tournamentId);

        Session session = AuthService.getSession(token);
        RegistrationDto dto = tournamentService.addMember(userId, tournamentId, session.getUserId());

        RegistrationResponse response = RegistrationResponse.builder()
                .tournamentId(dto.getTournamentId())
                .username(dto.getUser().getUsername())
                .build();

        logger.info("Method: addMember - Response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{tournamentId}/members")
    public ResponseEntity<MembersResponse> getMembers(@RequestHeader("Authorization") String token,
                                                      @PathVariable Long tournamentId) {
        logger.info("Method: getMembers - Request: token={}, tournamentId={}", token, tournamentId);

        List<UserDto> usersDto = tournamentService.getMembers(tournamentId);

        List<UserResponse> users = usersDto.stream().map(this::buildResponse).collect(Collectors.toList());

        MembersResponse response = MembersResponse.builder()
                .members(users)
                .build();

        logger.info("Method: getMembers - Response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping ("/{tournamentId}/join")
    public ResponseEntity<RegistrationResponse> join(@RequestHeader("Authorization") String token,
                                                     @PathVariable Long tournamentId) {
        logger.info("Method: join - Request: token={}, tournamentId={}", token, tournamentId);

        Session session = AuthService.getSession(token);
        RegistrationDto dto = tournamentService.join(session.getUserId(), tournamentId);

        RegistrationResponse response = RegistrationResponse.builder()
                .tournamentId(dto.getTournamentId())
                .username(dto.getUser().getUsername())
                .build();

        logger.info("Method: addMember - Response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("submitResults")
    public ResponseEntity<String> submitResults(@RequestHeader("Authorization") String token,
                                                @RequestBody SubmitResultRequest request) {
        logger.info("Method: submitResults - Request: token={}, request={}", token, request);
        Session session = AuthService.getSession(token);

        if(request.getResult() > 7 || request.getResult() < 1){
            throw new BusinessException("Solo se pueden cargar resultados del 1 al 7");
        }

        ResultDto dto = ResultDto.builder()
                .language(request.getLanguage())
                .result(request.getResult())
                .userId(request.getUserId())
                .build();
        tournamentService.submitResults(session.getUserId(), dto);

        logger.info("Method: submitResults - submit OK");

        return new ResponseEntity<>("Resultados cargados correctamente", HttpStatus.OK);
    }

    @GetMapping("/{tournamentId}/ranking")
    public ResponseEntity<RankingResponse> getRanking(@RequestHeader("Authorization") String token,
                                                      @PathVariable Long tournamentId,
                                                      @RequestParam(required = false) Integer pageNumber,
                                                      @RequestParam(required = false) Integer maxResults
                                                      ) {
        logger.info("Method: getRanking - Request: token={}, tournamentId={}", token, tournamentId);

        if(pageNumber == null || maxResults == null){
            pageNumber = 1;
            maxResults = 100;
        }

        Integer totalPages = tournamentService.getRankingTotalPages(tournamentId, maxResults);
        List<Punctuation> orderedPunctuations = tournamentService.getRanking(tournamentId, pageNumber, maxResults);

        RankingResponse response = RankingResponse.builder()
                .idTournament(tournamentId)
                .punctuations(orderedPunctuations)
                .pageNumber(pageNumber)
                .maxResults(maxResults)
                .totalPages(totalPages)
                .build();

        logger.info("Method: getRanking - Response: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{tournamentId}/ranking/myScore")
    public ResponseEntity<Punctuation> getMyScore(@RequestHeader("Authorization") String token,
                                                  @PathVariable Long tournamentId) {
        logger.info("Method: getMyScore - Request: token={}, tournamentId={}", token, tournamentId);

        Session session = AuthService.getSession(token);
        Punctuation response = tournamentService.getScoreFromUser(tournamentId, session.getUsername());

        logger.info("Method: getMyScore - Response: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{state}")
    public ResponseEntity<TournamentsListResponse> findUserTournamentsByStateWithPagination(@RequestHeader("Authorization") String token,
                                                                                            @PathVariable State state,
                                                                                            @RequestParam(required = false) Integer pageNumber,
                                                                                            @RequestParam(required = false) Integer maxResults){
        logger.info("Method: findUserTournamentsByStateWithPagination - Request: token={}, state={}, pageNumber={}, maxResults={}", token, state, pageNumber, maxResults);

        Session session = AuthService.getSession(token);
        if(pageNumber == null || maxResults == null){
            pageNumber = 1;
            maxResults = 100;
        }

        Integer totalPages = tournamentService.userTournamentsByStateTotalPages(session.getUserId(), state, maxResults);
        List<TournamentDto> tournamentsDto = tournamentService.findUserTournamentsByStateWithPagination(session.getUserId(), state, pageNumber, maxResults);

        List<TournamentResponse> tournaments = tournamentsDto
                .stream().map(this::buildResponse).collect(Collectors.toList());

        TournamentsListResponse response = TournamentsListResponse.builder()
                .tournaments(tournaments)
                .maxResults(maxResults)
                .pageNumber(pageNumber)
                .totalPages(totalPages)
                .build();

        logger.info("Method: findUserTournamentsByStateWithPagination - Response: {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public TournamentResponse buildResponse(TournamentDto dto) {
        return TournamentResponse.builder()
                .tourneyId(dto.getTourneyId())
                .name(dto.getName())
                .language(dto.getLanguage())
                .type(dto.getType())
                .state(dto.getState())
                .start(dto.getStart())
                .finish(dto.getFinish())
                .owner(dto.getOwner())
                .build();
    }

    public UserResponse buildResponse (UserDto dto) {
        return UserResponse.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .build();
    }
}
