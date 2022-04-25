package utn.frba.wordle.model;

public enum TournamentType {
    PRIVATE("Private"),
    PUBLIC("Public");

    String type;

    TournamentType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
