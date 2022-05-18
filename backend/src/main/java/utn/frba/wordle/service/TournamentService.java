package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.entity.RegistrationEntity;
import utn.frba.wordle.entity.TournamentEntity;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.exception.SessionJWTException;
import utn.frba.wordle.model.Punctuation;
import utn.frba.wordle.model.Ranking;
import utn.frba.wordle.model.State;
import utn.frba.wordle.repository.TournamentRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class TournamentService {

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    UserService userService;

    @Autowired
    PunctuationService punctuationService;

    public TournamentDto create(TournamentDto dto, Long userId) {

        UserDto owner;
        try {
            owner = userService.findUser(userId);
        } catch (NoSuchElementException e) {
            throw new SessionJWTException("El token de sesion jwt enviado, no coincide con usuarios existentes");
        }
        dto.setOwner(owner);
        TournamentEntity newTournament = mapToEntity(dto);

        TournamentEntity existingActiveTournament = tournamentRepository.findByName(dto.getName());
        if (existingActiveTournament != null) {
            throw new BusinessException("There is already an active Tournament with this name.");
        }
        newTournament.setState(State.READY);
        newTournament = tournamentRepository.save(newTournament);

        return mapToDto(newTournament);
    }

    @Transactional
    public MemberNewDto addMember(Long userId, Long tourneyID, Long ownerUserId) {

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

        Set<UserDto> members = userService.getTournamentMembers(tournamentEntity.getId());
        UserDto existingUser = members.stream().filter(member -> member.getUsername().equals(userEntity.getUsername())).findAny().orElse(null); //TODO hacer la busqueda directo en la query a la base de datos?
        if (existingUser != null) {
            throw new BusinessException("The user '"+userEntity.getUsername()+"' is already a member of the tournament "+tournamentEntity.getName());
        }


        RegistrationEntity registrationEntity = registrationService.addMember(tournamentEntity.getId(), userEntity.getId(), new Date());
        if(tournamentEntity.getRegistrations() == null){
            tournamentEntity.setRegistrations(new HashSet<>());
        }
        tournamentEntity.getRegistrations().add(registrationEntity);
        tournamentEntity = tournamentRepository.save(tournamentEntity);

        return MemberNewDto.builder()
                .tournamentId(tournamentEntity.getId())
                .username(userEntity.getUsername())
                .build();
    }

    @Transactional
    public JoinDto join(Long userId, Long tournamentId) {
        TournamentEntity tournamentEntity = tournamentRepository.findById(tournamentId).orElse(null);
        //TODO verificar que solo pueda entrar a torneos publicos
        if (tournamentEntity == null) {
            throw new BusinessException("The specified Tournament doesn't exist.");
        }

        List<RegistrationDto> registrations = registrationService.getRegistrationsFromUser(userId);
        boolean userAlreadyJoined = registrations.stream().anyMatch(m -> m.getUser().getId().equals(userId));
        if(userAlreadyJoined){
            throw new BusinessException("The user already joined the Tournament.");
        }

        tournamentRepository.addMember(tournamentEntity.getId(), userId, new Date());

        return JoinDto.builder()
                .tournamentID(tournamentId)
                .userID(userId)
                .build();
    }

    public TourneysDto listPublicTournaments() {
        List<TournamentEntity> tournaments = tournamentRepository.getPublicTournaments();

        return TourneysDto.builder()
                .tourneys(mapToDto(tournaments))
                .build();
    }

    public void submitResults(Long userId, ResultDto result) {
        punctuationService.submitResults(userId, result);
    }

    public Ranking getRanking(Long tourneyId) {
        List<RegistrationDto> registrations = registrationService.getRegistrationsFromTournament(tourneyId);
        List<Punctuation> punctuations = new ArrayList<>();
        registrations.forEach(
            registration -> {
                Integer sum = registration.getPunctuations().stream()
                        .reduce(0,
                                (acum, punctuation) ->
                                        acum + punctuation.getPunctuation().intValue(),
                                Integer::sum);
                Punctuation punctuation = Punctuation.builder()
                        .punctuation(sum.longValue())
                        .user(registration.getUser().getUsername())
                        .build();
                punctuations.add(punctuation);
            }
        );
        List<Punctuation> orderedPunctuations = punctuations.stream()
                .sorted(Comparator.comparingLong(Punctuation::getPunctuation).reversed())
                .collect(Collectors.toList());
        return Ranking.builder()
                .idTournament(tourneyId)
                .punctuations(orderedPunctuations)
                .build();
    }

    public List<TournamentDto> findUserTournamentsByState(Long userId, State state) {
        return mapToDto(tournamentRepository.findUserTournamentsByState(userId, state.name()));
    }

    public TournamentEntity mapToEntity(TournamentDto dto) {
        return TournamentEntity.builder()
                .id(dto.getTourneyId())
                .finish(dto.getFinish())
                .start(dto.getStart())
                .language(dto.getLanguage())
                .name(dto.getName())
                .state(dto.getState())
                .type(dto.getType())
                .owner(UserService.mapToEntity(dto.getOwner()))
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
