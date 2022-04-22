package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.entity.TournamentEntity;
import utn.frba.wordle.entity.UserEntity;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.model.TounamentType;
import utn.frba.wordle.repository.TournamentRepository;

import java.util.Collections;
import java.util.Date;

@Service
@NoArgsConstructor
public class TournamentService {

    @Autowired
    TournamentRepository tournamentRepository;

    public TournamentDto create(TournamentDto dto) {

        TournamentEntity newTournament = mapToEntity(dto);

        newTournament = tournamentRepository.save(newTournament);

        return mapToDto(newTournament);
    }

    private TournamentEntity mapToEntity(TournamentDto dto) {
        return TournamentEntity.builder()
                .finish(dto.getFinish())
                .start(dto.getStart())
                .language(dto.getLanguage())
                .name(dto.getName())
                .type(dto.getType())
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
                .build();
    }

    public MemberDto addMember(MemberDto memberDto) {
        return MemberDto.builder()
                .tournamentId(2)
                .username("Jorge")
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
                .tourneyId(1)
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
}
