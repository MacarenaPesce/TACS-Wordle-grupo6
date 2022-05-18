package utn.frba.wordle.model;

public enum TournamentType {
    PRIVATE("Private"),
    PUBLIC("Public");

    final String type;

    TournamentType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
