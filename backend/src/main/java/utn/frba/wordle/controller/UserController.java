package utn.frba.wordle.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.dto.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @GetMapping ("getPositions")
    public ResponseEntity<PositionsResponseDto> getPositions() {

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

        PositionsResponseDto dto = PositionsResponseDto
                .builder()
                .tourneys(Collections.singletonList(ladderboardDtos))
                .build();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
