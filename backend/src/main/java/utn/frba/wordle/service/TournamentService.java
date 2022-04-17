package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.JoinDto;
import utn.frba.wordle.dto.MemberDto;
import utn.frba.wordle.dto.TournamentDto;

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
}
