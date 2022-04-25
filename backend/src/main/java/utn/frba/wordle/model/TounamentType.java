package utn.frba.wordle.model;

public enum TounamentType {
    PRIVATE("Private"),
    PUBLIC("Public");

    String type;

    TounamentType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
