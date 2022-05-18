package utn.frba.wordle.model;

public enum State {
    READY("Ready"),
    STARTED("Started"),
    FINISHED("Finished");

    final String status;

    State(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
