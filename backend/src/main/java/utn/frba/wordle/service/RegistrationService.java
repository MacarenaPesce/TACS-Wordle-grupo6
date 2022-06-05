package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.model.dto.RegistrationDto;
import utn.frba.wordle.model.entity.PunctuationEntity;
import utn.frba.wordle.model.entity.RegistrationEntity;
import utn.frba.wordle.model.entity.TournamentEntity;
import utn.frba.wordle.model.entity.UserEntity;
import utn.frba.wordle.repository.RegistrationRepository;
import utn.frba.wordle.repository.TournamentRepository;
import utn.frba.wordle.repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@NoArgsConstructor
public class RegistrationService {

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    UserRepository userRepository;

    public List<RegistrationDto> getRegistrationsFromUser(Long userId) {
        return mapToDto(getRegistrationsEntityFromUser(userId));
    }

    public List<RegistrationEntity> getRegistrationsEntityFromUser(Long userId) {
        return registrationRepository.getAllByUser(userId);
    }

    public List<RegistrationEntity> getActiveRegistrationsEntityFromUser(Long userId) {
        Date dateToday = new Date();
        return registrationRepository.getActiveRegistrationsFromUser(userId, dateToday);
    }

    public List<RegistrationDto> getRegistrationsFromTournament(Long tourneyId) {
        return mapToDto(registrationRepository.getAllByTournament(tourneyId));
    }

    public RegistrationEntity addMember(Long tournamentId, Long userId, Date date) {
        TournamentEntity tournamentEntity = tournamentRepository.findById(tournamentId).orElseThrow();
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        RegistrationEntity entity = RegistrationEntity.builder()
                .registered(new Date())
                .tournament(tournamentEntity)
                .totalScore(0L)
                .daysPlayed(0L)
                .user(userEntity)
                .build();
        return registrationRepository.save(entity);
    }

    public void updateValues(RegistrationDto registration) {
        RegistrationEntity entity = registrationRepository.findById(registration.getId()).orElseThrow();
        entity.setTotalScore(registration.getTotalScore());
        entity.setDaysPlayed(registration.getDaysPlayed());
        registrationRepository.save(entity);
    }

    public RegistrationDto mapToDto(RegistrationEntity entity) {
        ArrayList<PunctuationEntity> punctuations = new ArrayList<>();
        if(entity.getPunctuations()!=null){
            punctuations.addAll(entity.getPunctuations());
        }

        return RegistrationDto.builder()
                .id(entity.getId())
                .tournamentId(entity.getTournament().getId())
                .punctuations(punctuations)
                .totalScore(entity.getTotalScore())
                .daysPlayed(entity.getDaysPlayed())
                .registered(entity.getRegistered())
                .user(entity.getUser())
                .build();
    }

    private List<RegistrationDto> mapToDto(List<RegistrationEntity> entities) {
        List<RegistrationDto> dtos = new ArrayList<>();
        for(RegistrationEntity entity: entities){
            dtos.add(mapToDto(entity));
        }
        return dtos;
    }
}
