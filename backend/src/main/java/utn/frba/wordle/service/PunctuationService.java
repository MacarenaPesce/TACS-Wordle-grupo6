package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.ResultDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.entity.PunctuationEntity;
import utn.frba.wordle.model.entity.RegistrationEntity;
import utn.frba.wordle.model.entity.UserEntity;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.repository.PunctuationRepository;
import utn.frba.wordle.repository.RegistrationRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static utn.frba.wordle.model.enums.ErrorMessages.RESULTS_ALREADY_SUBMITTED;

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

        List<PunctuationEntity> results = punctuationRepository.findTodayResults(userEntity.getId(), now, result.getLanguage());

        if(results != null && !results.isEmpty()){
            throw new BusinessException(RESULTS_ALREADY_SUBMITTED);
        }
        
        List<RegistrationEntity> registrations = registrationService.getActiveRegistrationsEntityFromUser(userId)
                .stream().filter(reg -> reg.getTournament().getLanguage().equals(result.getLanguage())).collect(Collectors.toList());

        PunctuationEntity punctuation = PunctuationEntity.builder()
                .punctuation(result.getResult())
                .language(result.getLanguage())
                .user(userEntity)
                .registrations(new HashSet<>())
                .date(LocalDate.now())
                .build();
        punctuation = punctuationRepository.save(punctuation);

        for (RegistrationEntity registration : registrations) {
            registration.getPunctuations().add(punctuation);
            registration.setDaysPlayed(registration.getDaysPlayed() + 1L);
            registration.setTotalScore(registration.getTotalScore() + punctuation.getPunctuation());
            registration.setLastSubmittedScore(new Date());
            registrationRepository.save(registration);
            punctuation.getRegistrations().add(registration);
            punctuationRepository.save(punctuation);
        }
    }

    public List<PunctuationEntity> getPunctuationsEntityFromTourney(Long idTournament) {
        return punctuationRepository.findResultsFromTournament(idTournament);
    }

    public Long getTodaysResult(Long userId, Language language) {
        LocalDate now = LocalDate.now();
        List<PunctuationEntity> results = punctuationRepository.findTodayResults(userId, now, language);
        return results.stream().findFirst().orElse(PunctuationEntity.builder().punctuation(0L).build()).getPunctuation();
    }
}
