package utn.frba.wordle.model;

public enum State {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    final String status;

    State(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
