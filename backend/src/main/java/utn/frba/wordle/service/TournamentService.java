package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.*;

import java.util.Collections;
import java.util.Date;

@Service
@NoArgsConstructor
public class TournamentService {

    public TournamentDto create(TournamentDto request) {

        return TournamentDto.builder()
                .name("Pepita")
                .language("ES")
                .tourneyId(1)
                .type("Public")
                .finish(new Date())
                .start(new Date())
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
                .language("ES")
                .tourneyId(1)
                .type("Public")
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
                .languaje("EN")
                .build();
    }
}
