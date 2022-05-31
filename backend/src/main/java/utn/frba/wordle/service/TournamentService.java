package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.exception.SessionJWTException;
import utn.frba.wordle.model.dto.RegistrationDto;
import utn.frba.wordle.model.dto.ResultDto;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.entity.RankingEntity;
import utn.frba.wordle.model.entity.RegistrationEntity;
import utn.frba.wordle.model.entity.TournamentEntity;
import utn.frba.wordle.model.entity.UserEntity;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.pojo.Punctuation;
import utn.frba.wordle.repository.RankingRepository;
import utn.frba.wordle.repository.TournamentRepository;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@NoArgsConstructor
public class TournamentService {

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    RankingRepository rankingRepository;

    @Autowired
    UserService userService;

    @Autowired
    PunctuationService punctuationService;

    @Transactional
    public TournamentDto create(TournamentDto dto, Long userId) {

        UserEntity owner;
        try {
            owner = userService.findUserEntity(userId);
        } catch (NoSuchElementException e) {
            throw new SessionJWTException("El token de sesion jwt enviado, no coincide con usuarios existentes");
        }
        TournamentEntity existingActiveTournament = tournamentRepository.findByName(dto.getName());
        if (existingActiveTournament != null) {
            throw new BusinessException("There is already an active Tournament with this name.");
        }
        TournamentEntity newTournament = mapToEntity(dto);
        newTournament.setRegistrations(new HashSet<>());
        newTournament.setState(State.READY);
        newTournament.setOwner(owner);
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
        //TODO verificar que solo pueda entrar a torneos publicos
        if (tournamentEntity == null) {
            throw new BusinessException("The specified Tournament doesn't exist.");
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

    public List<TournamentDto> findPublicActiveTournaments(String name) {
        List<TournamentEntity> tournaments = tournamentRepository.findPublicActiveTournamentsByName(name.toLowerCase());

        return mapToDto(tournaments);
    }

    public List<TournamentDto> listPublicActiveTournaments() {
        List<TournamentEntity> tournaments = tournamentRepository.getPublicActiveTournaments();

        return mapToDto(tournaments);
    }

    public void submitResults(Long userId, ResultDto result) {
        punctuationService.submitResults(userId, result);
    }

    public List<Punctuation> getRanking(Long tourneyId) {
        updateTournamentScores(tourneyId);

        List<RankingEntity> rankingEntities = rankingRepository.getScores(tourneyId);

        return mapRankingToDto(rankingEntities);
    }

    private void updateTournamentScores(Long tourneyId) {
        List<RegistrationDto> registrations = registrationService.getRegistrationsFromTournament(tourneyId);

        TournamentDto tournament = getTournamentFromId(tourneyId);
        long diff = tournament.getFinish().getTime() - tournament.getStart().getTime();
        TimeUnit time = TimeUnit.DAYS;
        long tournamentDuration = time.convert(diff, TimeUnit.MILLISECONDS);
        registrations.forEach(registration -> {
            long notPlayedDays = (tournamentDuration - registration.getDaysPlayed());
            if (notPlayedDays > 0) {
                registration.setTotalScore(registration.getTotalScore() + notPlayedDays * 7);
                registration.setDaysPlayed(registration.getDaysPlayed() + notPlayedDays);
                registrationService.updateValues(registration);
            }
        });
    }

    public List<TournamentDto> findUserTournamentsByState(Long userId, State state) {
        return mapToDto(tournamentRepository.findUserTournamentsByState(userId, state.name()));
    }

    public List<TournamentDto> getActiveTournamentsFromUser(Long userId) {
        return mapToDto(tournamentRepository.findActiveTournamentsFromUser(userId));
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
        rankingEntities.forEach(entity -> {
            punctuations.add(mapRankingToDto(entity));
        });
        return punctuations;
    }

    private Punctuation mapRankingToDto(RankingEntity entity) {
        return Punctuation.builder()
                .punctuation(entity.getTotalScore())
                .user(entity.getUsername())
                .position(entity.getPosition())
                .build();
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
                .state(dto.getState())
                .type(dto.getType())
                .owner(user)
                .build();
    }

    private TournamentDto mapToDto(TournamentEntity entity) {
        return TournamentDto.builder()
                .language(entity.getLanguage())
                .name(entity.getName())
                .finish(entity.getFinish())
                .start(entity.getStart())
                .state(entity.getState())
                .tourneyId(entity.getId())
                .type(entity.getType())
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
}
