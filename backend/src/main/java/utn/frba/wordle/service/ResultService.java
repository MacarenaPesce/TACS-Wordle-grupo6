package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.ResultDto;
import utn.frba.wordle.dto.UserDto;
import utn.frba.wordle.entity.PunctuationEntity;
import utn.frba.wordle.entity.RegistrationEntity;
import utn.frba.wordle.entity.UserEntity;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.repository.ResultRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationService registrationService;

    public void submitResults(Long userId, ResultDto resultDto) {

        UserDto user = userService.findUser(userId);
        UserEntity userEntity = UserService.mapToEntity(user);
        LocalDate now = LocalDate.now();

        List<PunctuationEntity> results = resultRepository.findTodayResults(userEntity.getId(), now);

        if(results != null && !results.isEmpty()){
            throw new BusinessException("The user already submitted his results.");
        }
        
        List<RegistrationEntity> registrations = registrationService.getRegistrationsEntityFromUser(userId);

        for(RegistrationEntity registration:registrations){
            PunctuationEntity entity = PunctuationEntity.builder()
                    .punctuation(resultDto.getResult())
                    .registration(registration)
                    .date(LocalDate.now())
                    .build();

            resultRepository.save(entity);

        }
    }

    public List<PunctuationEntity> getPunctuationsEntityFromTourney(Long idTournament) {
        return resultRepository.findResultsFromTournament(idTournament);
    }
}
