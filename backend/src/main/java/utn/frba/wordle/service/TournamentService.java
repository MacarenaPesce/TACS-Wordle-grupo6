package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.entity.TournamentEntity;
import utn.frba.wordle.entity.UserEntity;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.State;
import utn.frba.wordle.repository.TournamentRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@NoArgsConstructor
public class TournamentService {

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    UserService userService;

    @Autowired
    ResultService resultsService;

    public TournamentDto create(TournamentDto dto, Long userId) {

        UserDto owner = userService.findUser(userId);
        dto.setOwner(owner);
        TournamentEntity newTournament = mapToEntity(dto);

        TournamentEntity existingActiveTournament = tournamentRepository.findByName(dto.getName());
        if(existingActiveTournament != null){
            throw new BusinessException("There is already an active Tournament with this name.");
        }
        newTournament.setState(State.ACTIVE);
        newTournament = tournamentRepository.save(newTournament);

        return mapToDto(newTournament);
    }

    @Transactional
    public MemberDto addMember(MemberDto memberDto, Long ownerUserId) {

        TournamentEntity tournamentEntity = tournamentRepository.findById(memberDto.getTournamentId()).orElse(null);

        if (tournamentEntity == null) {
            throw new BusinessException("El torneo especificado no existe.");
        }
        if (!tournamentEntity.getOwner().getId().equals(ownerUserId)) {
            throw new BusinessException("Solo puedes agregar miembros a un torneo que tu hayas creado.");
        }

        UserEntity userEntity = userService.findUserByUsername(memberDto.getUsername());
        //TODO validar existencia de usuario a agregar
        //TODO validar que el usuario a agregar ya no este incluido en el torneo
//        Set<UserDto> members = userService.getTournamentMembers(tournamentEntity.getId());

        tournamentRepository.addMember(tournamentEntity.getId(), userEntity.getId());

        return MemberDto.builder()
                .tournamentId(tournamentEntity.getId())
                .username(userEntity.getUsername())
                .build();
    }

    @Transactional
    public JoinDto join(Long userId, Long tournamentId) {
        TournamentEntity tournamentEntity = tournamentRepository.findById(tournamentId).orElse(null);

        if (tournamentEntity == null) {
            throw new BusinessException("The specified Tournament doesn't exist.");
        }

        boolean userAlreadyJoined = tournamentEntity.getMembers().stream().anyMatch(m -> m.getId().equals(userId));
        if(userAlreadyJoined){
            throw new BusinessException("The user already joined the Tournament.");
        }

        tournamentRepository.addMember(tournamentEntity.getId(), userId);

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

    public ResultDto submitResults(Long userId, ResultDto resultDto) {

        return resultsService.submitResults(userId, resultDto);
    }

    private TournamentEntity mapToEntity(TournamentDto dto) {
        return TournamentEntity.builder()
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
