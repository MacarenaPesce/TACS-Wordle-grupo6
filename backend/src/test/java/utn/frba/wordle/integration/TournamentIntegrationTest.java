package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.dto.LoginDto;
import utn.frba.wordle.dto.MemberDto;
import utn.frba.wordle.dto.TournamentDto;
import utn.frba.wordle.dto.UserDto;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.model.TounamentType;
import utn.frba.wordle.service.TournamentService;
import utn.frba.wordle.service.UserService;

import java.util.Date;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TournamentIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    TournamentService tournamentService;

    @Autowired
    UserService userService;

    @Test
    public void aUserCanCreateATournament(){
        String name = "TorneoPrueba";
        LoginDto user = LoginDto.builder()
                .email("mail@mail.com")
                .username("usernameTest")
                .build();
        UserDto owner = userService.createUser(user);
        TournamentDto dto = TournamentDto.builder()
                .type(TounamentType.PRIVATE)
                .start(new Date())
                .finish(new Date())
                .name(name)
                .language(Language.ES)
                .build();

        dto = tournamentService.create(dto, owner.getId());

        assertThat(dto).hasNoNullFieldsOrProperties();
        assertEquals(dto.getName(), name);
    }

    @Test
    public void aUserCantAddAnotherUserToANonexistentTournament(){
        LoginDto owner = LoginDto.builder()
                .email("mail@mail.com")
                .username("usernameTest")
                .build();
        UserDto userOwner = userService.createUser(owner);
        LoginDto user = LoginDto.builder()
                .email("mail@mail.com2")
                .username("usernameTest2")
                .build();
        userService.createUser(user);
        MemberDto newMember = MemberDto.builder()
                .username("Richard")
                .tournamentId(32167L)
                .build();

        assertThrows(BusinessException.class, () -> tournamentService.addMember(newMember, userOwner.getId()));

    }
/*
    @Test
    public void aUserCantAddAnotherUserToATournamentThatDontOwn(){

    }

    @Test
    public void aUserCanAddAnotherUserToATournamentThatOwns(){

    }*/
}

