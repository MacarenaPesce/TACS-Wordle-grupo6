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
import utn.frba.wordle.model.Language;
import utn.frba.wordle.repository.PunctuationRepository;
import utn.frba.wordle.repository.RegistrationRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class PunctuationService {

    @Autowired
    private PunctuationRepository punctuationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private RegistrationRepository registrationRepository;

    public void submitResults(Long userId, ResultDto result) {

        UserDto user = userService.findUser(userId);
        UserEntity userEntity = UserService.mapToEntity(user);
        LocalDate now = LocalDate.now();

        List<PunctuationEntity> results = punctuationRepository.findTodayResults(userEntity.getId(), now);

        if(results != null && !results.isEmpty()){
            throw new BusinessException("The user already submitted his results.");
        }
        
        List<RegistrationEntity> registrations = registrationService.getRegistrationsEntityFromUser(userId)
                .stream().filter(reg -> reg.getTournament().getLanguage().equals(result.getLanguage())).collect(Collectors.toList());

        for(RegistrationEntity registration:registrations){
            PunctuationEntity entity = PunctuationEntity.builder()
                    .punctuation(result.getResult())
                    .language(result.getLanguage())
                    .user(userEntity)
                    .date(LocalDate.now())
                    .build();
            if(entity.getRegistrations() == null){
                entity.setRegistrations(new HashSet<>());
            }


            entity = punctuationRepository.save(entity);
            registration.getPunctuations().add(entity);
            registrationRepository.save(registration);
            entity.getRegistrations().add(registration);
            punctuationRepository.save(entity);
        }
    }

    public List<PunctuationEntity> getPunctuationsEntityFromTourney(Long idTournament) {
        return punctuationRepository.findResultsFromTournament(idTournament);
    }

    public Long getTodaysResult(Long userId, Language language) {
        LocalDate now = LocalDate.now();
        List<PunctuationEntity> results = punctuationRepository.findTodayResults(userId, now);
        return results.stream().filter(punctuationEntity -> punctuationEntity.getLanguage().equals(language))
                .findFirst().orElse(PunctuationEntity.builder().punctuation(0L).build()).getPunctuation();
    }
}