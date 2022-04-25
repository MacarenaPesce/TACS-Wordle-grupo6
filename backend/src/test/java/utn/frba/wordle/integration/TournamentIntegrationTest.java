package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.dto.LoginDto;
import utn.frba.wordle.dto.MemberDto;
import utn.frba.wordle.dto.TournamentDto;
import utn.frba.wordle.dto.UserDto;
import utn.frba.wordle.entity.UserEntity;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.model.TounamentType;
import utn.frba.wordle.repository.TournamentRepository;
import utn.frba.wordle.service.TournamentService;
import utn.frba.wordle.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;


public class TournamentIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    TournamentService tournamentService;

    @Autowired
    TournamentRepository tournamentRepository;

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
    @Test
    public void aUserCantAddAnotherUserToATournamentThatDontOwn(){
        String name = "TorneoPrueba";
        LoginDto owner = LoginDto.builder()
                .email("mail@mail.com")
                .username("usernameTest")
                .build();
        UserDto ownerUser =userService.createUser(owner);
        LoginDto user = LoginDto.builder()
                .email("mail@mail.com2")
                .username("usernameTest2")
                .build();
        UserDto magicUser = userService.createUser(user);
        MemberDto newMember = MemberDto.builder()
                .username("Richard")
                .tournamentId(32167L)
                .build();
        TournamentDto dto = TournamentDto.builder()
                .type(TounamentType.PRIVATE)
                .start(new Date())
                .finish(new Date())
                .name(name)
                .language(Language.ES)
                .owner(magicUser)
                .build();
        tournamentService.create(dto, ownerUser.getId());

        assertThrows(BusinessException.class, () -> tournamentService.addMember(newMember, ownerUser.getId()));

    }

    @Test
    public void aUserCanAddAnotherUserToATournamentThatOwns(){
        String name = "TorneoPrueba";
        LoginDto owner = LoginDto.builder()
                .email("mail@mail.com")
                .username("usernameTest")
                .build();
        UserDto ownerUser = userService.createUser(owner);
        LoginDto user = LoginDto.builder()
                .email("mail@mail.com2")
                .username("usernameTest2")
                .build();
        UserDto player = userService.createUser(user);
        TournamentDto dto = TournamentDto.builder()
                .type(TounamentType.PRIVATE)
                .start(new Date())
                .finish(new Date())
                .name(name)
                .language(Language.ES)
                .owner(ownerUser)
                .build();
        dto = tournamentService.create(dto, ownerUser.getId());
        MemberDto newMember = MemberDto.builder()
                .username(player.getUsername())
                .tournamentId(dto.getTourneyId())
                .build();

        tournamentService.addMember(newMember, ownerUser.getId());

        Set<UserDto> members = userService.getTournamentMembers(dto.getTourneyId());
        assertThat(members).contains(player);
    }
}

