package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.entity.TournamentEntity;
import utn.frba.wordle.entity.UserEntity;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.model.TounamentType;
import utn.frba.wordle.repository.TournamentRepository;

import java.util.*;

@Service
@NoArgsConstructor
public class TournamentService {

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    UserService userService;

    public TournamentDto create(TournamentDto dto, Long userId) {

        UserDto owner = userService.findUser(userId);
        dto.setOwner(owner);
        TournamentEntity newTournament = mapToEntity(dto);

        newTournament = tournamentRepository.save(newTournament);

        return mapToDto(newTournament);
    }

    @Transactional
    public MemberDto addMember(MemberDto memberDto, Long ownerUserId) {

        TournamentEntity tournamentEntity = tournamentRepository.findById(memberDto.getTournamentId()).orElse(null);

        if (tournamentEntity == null) {
            throw new BusinessException("The specified Tournament doesn't exist.");
        }
        if (!tournamentEntity.getOwner().getId().equals(ownerUserId)) {
            throw new BusinessException("The user doesn't own the specified Tournament.");
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

    public JoinDto join(Integer id) {
        return JoinDto.builder()
                .tournamentID(id)
                .userID(2)
                .build();
    }

    public TourneysDto listPublicTournaments() {
        TournamentDto tournamentDto = TournamentDto.builder()
                .name("Pepita")
                .language(Language.ES)
                .tourneyId(1L)
                .type(TounamentType.PUBLIC)
                .finish(new Date())
                .start(new Date())
                .build();

        return TourneysDto.builder()
                .tourneys(Collections.singletonList(tournamentDto))
                .build();
    }

    public ResultDto submitResults(ResultDto resultDto) {
        return ResultDto.builder()
                .result(2)
                .language(Language.EN)
                .build();
    }


    private TournamentEntity mapToEntity(TournamentDto dto) {
        return TournamentEntity.builder()
                .finish(dto.getFinish())
                .start(dto.getStart())
                .language(dto.getLanguage())
                .name(dto.getName())
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
                .tourneyId(entity.getId())
                .type(entity.getType())
                .owner(UserService.mapToDto(entity.getOwner()))
                .build();
    }
}
