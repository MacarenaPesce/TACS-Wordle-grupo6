package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.exception.SessionJWTException;
import utn.frba.wordle.model.dto.RegistrationDto;
import utn.frba.wordle.model.dto.ResultDto;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.entity.*;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.enums.TournamentType;
import utn.frba.wordle.model.http.FindTournamentsFilters;
import utn.frba.wordle.model.pojo.Punctuation;
import utn.frba.wordle.repository.RankingRepository;
import utn.frba.wordle.repository.TournamentRepository;
import utn.frba.wordle.repository.TournamentRepositoryCustomImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@NoArgsConstructor
public class TournamentService {

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    TournamentRepositoryCustomImpl tournamentRepositoryCustom;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    RankingRepository rankingRepository;

    @Autowired
    UserService userService;

    @Autowired
    PunctuationService punctuationService;

    @SneakyThrows
    @Transactional
    public TournamentDto create(TournamentDto dto, Long userId) {

        UserEntity owner;
        try {
            owner = userService.findUserEntity(userId);
        } catch (NoSuchElementException e) {
            throw new SessionJWTException("El token de sesion jwt enviado, no coincide con usuarios existentes");
        }
        TournamentEntity existingActiveTournament = tournamentRepository.getActiveTournamentsByName(dto.getName());
        if (existingActiveTournament != null) {
            throw new BusinessException("There is already an active Tournament with this name.");
        }
        Date startDate = parseStartDate(dto.getStart());
        Date finishDate = parseFinishDate(dto.getFinish());

        TournamentEntity newTournament = mapToEntity(dto);
        newTournament.setRegistrations(new HashSet<>());
        newTournament.setOwner(owner);
        newTournament.setStart(startDate);
        newTournament.setFinish(finishDate);
        newTournament = tournamentRepository.save(newTournament);

        addMember(userId, newTournament.getId(), userId);

        return mapToDto(newTournament);
    }

    @Transactional
    public RegistrationDto addMember(Long userId, Long tourneyID, Long ownerUserId) {

        TournamentEntity tournamentEntity = tournamentRepository.findById(tourneyID).orElse(null);

        if (tournamentEntity == null) {
            throw new BusinessException("El torneo especificado no existe.");
        }
        if (!tournamentEntity.getOwner().getId().equals(ownerUserId)) {
            throw new BusinessException("Solo puedes agregar miembros a un torneo que tu hayas creado.");
        }

        UserDto userEntity = userService.findUser(userId);
        if(userEntity == null){
            throw new BusinessException("El usuario especificado no se encuentra registrado en el sistema");
        }

        List<UserDto> members = userService.getTournamentMembers(tournamentEntity.getId());
        UserDto existingUser = members.stream().filter(member -> member.getUsername().equals(userEntity.getUsername())).findAny().orElse(null); //TODO hacer la busqueda directo en la query a la base de datos?
        if (existingUser != null) {
            throw new BusinessException("The user '"+userEntity.getUsername()+"' is already a member of the tournament "+tournamentEntity.getName());
        }

        RegistrationEntity registrationEntity = registrationService.addMember(tournamentEntity.getId(), userEntity.getId(), new Date());
        if(tournamentEntity.getRegistrations() == null){
            tournamentEntity.setRegistrations(new HashSet<>());
        }
        tournamentEntity.getRegistrations().add(registrationEntity);
        tournamentRepository.save(tournamentEntity);

        return registrationService.mapToDto(registrationEntity);
    }

    @Transactional
    public RegistrationDto join(Long userId, Long tournamentId) {
        TournamentEntity tournamentEntity = tournamentRepository.findById(tournamentId).orElse(null);

        if (tournamentEntity == null) {
            throw new BusinessException("The specified Tournament doesn't exist.");
        }
        if (tournamentEntity.getType().equals(TournamentType.PRIVATE)) {
            throw new BusinessException("You can not join a PRIVATE tournament.");
        }

        List<RegistrationDto> registrations = registrationService.getRegistrationsFromUser(userId);
        boolean userAlreadyJoined = registrations.stream()
                            .filter(registrationDto -> registrationDto.getTournamentId().equals(tournamentId))
                            .anyMatch(m -> m.getUser().getId().equals(userId));
        if(userAlreadyJoined){
            throw new BusinessException("The user already joined the Tournament.");
        }

        RegistrationEntity registrationEntity = registrationService.addMember(tournamentEntity.getId(), userId, new Date());

        return registrationService.mapToDto(registrationEntity);
    }

    public List<TournamentDto> findPublicActiveTournaments(String name, Integer pageNumber, Integer maxResults) {
        Integer offset = (pageNumber - 1) * maxResults;
        List<TournamentEntity> tournaments = tournamentRepository.findPublicActiveTournamentsByName(name.toLowerCase(), offset, maxResults);

        return mapToDto(tournaments);
    }

    public List<TournamentDto> findTournaments(FindTournamentsFilters filters) {
        List<TournamentEntity> tournaments = tournamentRepositoryCustom.findTournaments(filters);
        return mapToDto(tournaments);
    }

    public Integer findTournamentsGetTotalPages(FindTournamentsFilters filters) {
        return tournamentRepositoryCustom.findTournamentsGetTotalPages(filters);
    }

    public Integer findPublicActiveTournamentsTotalPages(String name, Integer maxResults) {
        Integer totalResults = tournamentRepository.findPublicActiveTournamentsByNameTotalPages(name.toLowerCase());
        int pages = totalResults / maxResults;
        return Math.toIntExact(Math.round(Math.ceil(pages)));
    }

    public List<TournamentDto> listPublicActiveTournaments(Integer pageNumber, Integer maxResults) {
        Integer offset = (pageNumber - 1) * maxResults;
        List<TournamentEntity> tournaments = tournamentRepository.getPublicActiveTournaments(offset, maxResults);

        return mapToDto(tournaments);
    }

    public void submitResults(Long userId, ResultDto result) {
        punctuationService.submitResults(userId, result);
    }

    public Integer getRankingTotalPages(Long tournamentId, Integer maxResults) {
        Integer totalResults = rankingRepository.getScoresTotalPages(tournamentId);
        int pages = totalResults / maxResults;
        return Math.toIntExact(Math.round(Math.ceil(pages)));
    }

    public List<Punctuation> getRanking(Long tourneyId, Integer pageNumber, Integer maxResults) {
        Integer offset = (pageNumber - 1) * maxResults;
        updateTournamentScores(tourneyId);

        Set<RankingEntity> rankingEntities = rankingRepository.getScores(tourneyId, offset, maxResults);

        return mapRankingToDto(new ArrayList<>(rankingEntities));
    }

    private void updateTournamentScores(Long tourneyId) {
        TournamentDto tournament = getTournamentFromId(tourneyId);

        if(tournament.getState().equals(State.READY)){
            //The tournament didn't started, don't update scores
            return;
        }

        List<RegistrationDto> registrations = registrationService.getOutdatedRegistrationsFromTournament(tournament);
        registrations.forEach(registration -> {
            LocalDate lastScoreDate = registration.getPunctuations().stream().map(PunctuationEntity::getDate).max(LocalDate::compareTo).orElse(null);
            long notPlayedDays = (tournament.getTournamentDuration() - registration.getDaysPlayed());
            if(!tournament.getState().equals(State.FINISHED) && (lastScoreDate == null || !isToday(lastScoreDate))){
                notPlayedDays = notPlayedDays - 1;
            }
            if (notPlayedDays > 0) {
                registration.setTotalScore(registration.getTotalScore() + notPlayedDays * 7);
                registration.setDaysPlayed(registration.getDaysPlayed() + notPlayedDays);
                registrationService.updateValues(registration);
            }
        });
    }

    public List<TournamentDto> findUserTournamentsByStateWithPagination(Long userId, State state, Integer pageNumber, Integer maxResults) {
        FindTournamentsFilters filters = FindTournamentsFilters.builder()
                .state(state)
                .userId(userId)
                .pageNumber(pageNumber)
                .maxResults(maxResults)
                .build();

        return findTournaments(filters);
    }

    public List<TournamentDto> findActiveTournamentsFromUser(Long userId, String name, Integer pageNumber, Integer maxResults) {
        Integer offset = (pageNumber - 1) * maxResults;
        return mapToDto(tournamentRepository.findActiveTournamentsFromUser(userId, name.toLowerCase(), offset, maxResults));
    }

    public List<TournamentDto> getActiveTournamentsFromUser(Long userId, Integer pageNumber, Integer maxResults) {
        Integer offset = (pageNumber - 1) * maxResults;
        return mapToDto(tournamentRepository.getActiveTournamentsFromUser(userId, offset, maxResults));
    }

    public TournamentDto getTournamentFromId(Long tournamentId) {
        return mapToDto(tournamentRepository.findById(tournamentId).orElseThrow());
    }

    public List<UserDto> getMembers(Long tournamentId) {
        return userService.getTournamentMembers(tournamentId);
    }

    public Punctuation getScoreFromUser(Long tournamentId, String username) {
        updateTournamentScores(tournamentId);
        return mapRankingToDto(rankingRepository.findScore(tournamentId, username));
    }

    private List<Punctuation> mapRankingToDto(List<RankingEntity> rankingEntities) {
        List<Punctuation> punctuations = new ArrayList<>();
        rankingEntities.forEach(entity -> punctuations.add(mapRankingToDto(entity)));
        return punctuations;
    }

    private Punctuation mapRankingToDto(RankingEntity entity) {

        Boolean scoreSubmittedToday = hasSubmittedScoreToday(entity);

        return Punctuation.builder()
                .punctuation(entity.getTotalScore())
                .user(entity.getUsername())
                .position(entity.getPosition())
                .submittedScoreToday(scoreSubmittedToday)
                .build();
    }

    private Date parseStartDate(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.parse(sdf.format(calendar.getTime()));
    }

    public Date parseFinishDate(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.SECOND, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.parse(sdf.format(calendar.getTime()));
    }

    private Boolean hasSubmittedScoreToday(RankingEntity rankingEntity) {

        if(rankingEntity.getLastSubmittedScore() == null){
            return false;
        }

        Date date = rankingEntity.getLastSubmittedScore();
        Calendar entityDate = Calendar.getInstance();
        entityDate.setTime(date);
        int year = entityDate.get(Calendar.YEAR);
        int month = entityDate.get(Calendar.MONTH) + 1;
        int day = entityDate.get(Calendar.DAY_OF_MONTH);

        return isToday(year, month, day);
    }

    private boolean isToday(LocalDate lastScoreDate) {
        int year = lastScoreDate.getYear();
        int month = lastScoreDate.getMonthValue();
        int day = lastScoreDate.getDayOfMonth();

        return isToday(year, month, day);
    }

    private boolean isToday(int year, int month, int day) {
        Date now = new Date();
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(now);
        int actualYear = calendarDate.get(Calendar.YEAR);
        int actualMonth = calendarDate.get(Calendar.MONTH) + 1;
        int actualDay = calendarDate.get(Calendar.DAY_OF_MONTH);

        return year == actualYear && month == actualMonth && day == actualDay;
    }

    public TournamentEntity mapToEntity(TournamentDto dto) {
        UserEntity user = null;
        if(dto.getOwner() != null) {
            user = UserService.mapToEntity(dto.getOwner());
        }
        return TournamentEntity.builder()
                .id(dto.getTourneyId())
                .finish(dto.getFinish())
                .start(dto.getStart())
                .language(dto.getLanguage())
                .name(dto.getName())
                .type(dto.getType())
                .owner(user)
                .build();
    }

    private TournamentDto mapToDto(TournamentEntity entity) {
        State state;
        Date now = new Date();
        if(now.before(entity.getStart())){
            state = State.READY;
        }
        else if(now.after(entity.getFinish())){
            state = State.FINISHED;
        }
        else {
            state = State.STARTED;
        }

        long todayTime = new Date().getTime();
        long startTime = entity.getStart().getTime();
        long finishTime = entity.getFinish().getTime();
        long diff = Long.min(todayTime, finishTime) - startTime;
        TimeUnit time = TimeUnit.DAYS;
        long tournamentDuration = time.convert(diff, TimeUnit.MILLISECONDS) + 1L;

        return TournamentDto.builder()
                .language(entity.getLanguage())
                .name(entity.getName())
                .finish(entity.getFinish())
                .start(entity.getStart())
                .state(state)
                .tourneyId(entity.getId())
                .type(entity.getType())
                .tournamentDuration(tournamentDuration)
                .owner(UserService.mapToDto(entity.getOwner()))
                .build();
    }

    private List<TournamentDto> mapToDto(List<TournamentEntity> entities) {
        List<TournamentDto> dtos = new ArrayList<>(Collections.emptySet());
        for(TournamentEntity tournament:entities){
            dtos.add(mapToDto(tournament));
        }
        return dtos;
    }

    public Integer userTournamentsByStateTotalPages(Long userId, State state, Integer maxResults) {
        FindTournamentsFilters filters = FindTournamentsFilters.builder()
                .state(state)
                .userId(userId)
                .maxResults(maxResults)
                .build();

        return findTournamentsGetTotalPages(filters);
    }

    public Integer getActiveTournamentsFromUserTotalPages(Long userId, Integer maxResults) {
        Integer totalResults = tournamentRepository.getActiveTournamentsFromUserTotalPages(userId);
        int pages = totalResults / maxResults;
        return Math.toIntExact(Math.round(Math.ceil(pages)));
    }

    public Integer findActiveTournamentsFromUserTotalPages(Long userId, String name, Integer maxResults) {
        Integer totalResults = tournamentRepository.findActiveTournamentsFromUserTotalPages(userId, name);
        int pages = totalResults / maxResults;
        return Math.toIntExact(Math.round(Math.ceil(pages)));
    }

    public Integer listPublicActiveTournamentsTotalPages(Integer maxResults) {
        Integer totalResults = tournamentRepository.listPublicActiveTournamentsTotalPages();
        int pages = totalResults / maxResults;
        return Math.toIntExact(Math.round(Math.ceil(pages)));
    }
}
