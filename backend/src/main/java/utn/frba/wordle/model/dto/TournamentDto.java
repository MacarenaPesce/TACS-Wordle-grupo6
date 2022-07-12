package utn.frba.wordle.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.enums.TournamentType;

import java.util.Date;
import java.util.Objects;

@Getter
@Builder
@ToString
public class TournamentDto {

    private final Long tourneyId;
    private String name;
    private Language language;
    private TournamentType type;
    private State state;
    private Date start;
    private Date finish;
    private final UserDto owner;
    private final Long tournamentDuration;

    public void setState(State state) {
        this.state = state;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLanguage(Language language) {
        this.language = language;
    }
    public void setType(TournamentType type) {
        this.type = type;
    }
    public void setStart(Date start) {
        this.start = start;
    }
    public void setFinish(Date finish) {
        this.finish = finish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TournamentDto)) return false;
        TournamentDto that = (TournamentDto) o;
        return Objects.equals(tourneyId, that.tourneyId) &&
                Objects.equals(name, that.name) &&
                language == that.language &&
                type == that.type &&
                Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tourneyId, name, language, type, owner);
    }

    public String toStringTelegramList(){
        return tourneyId+" - "+name+" - "+state;
    }

    public String toStringInfoMarkdownV1(){
        return "_Presentamos la info del torneo número "+tourneyId+"_\n\n"+
                "*Llamado*: \n"+name+"\n\n"+
                "*Creado por*: "+owner.toStringTelegram().replace('_', ' ')+"\n\n"+
                "*Idioma*: "+language+"\n"+
                "*Tipo*: "+type+"\n\n"+
                "*Estado actual*: "+state+"\n\n"+
                "*Inicio*: "+start+"\n"+
                "*Fin*: "+finish+"\n"
                //+"*Duración*: "+tournamentDuration*(-1)+" días\n"
                ;
    }
}
