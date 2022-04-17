package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.LadderboardDto;
import utn.frba.wordle.dto.PositionDto;
import utn.frba.wordle.dto.PositionsResponseDto;

import java.util.Collections;

@Service
@NoArgsConstructor
public class UserService {


    public PositionsResponseDto getPositions() {
        PositionDto positionsDto = PositionDto
                .builder()
                .userId(1)
                .points(10)
                .username("carlita")
                .build();

        LadderboardDto ladderboardDtos = LadderboardDto
                .builder()
                .name("pepis")
                .tourneyId(2)
                .positions(Collections.singletonList(positionsDto))
                .build();

        return PositionsResponseDto
                .builder()
                .tourneys(Collections.singletonList(ladderboardDtos))
                .build();
    }
}
