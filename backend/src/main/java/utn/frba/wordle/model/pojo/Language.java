package utn.frba.wordle.model.pojo;

public enum Language {
    EN("English"),
    ES("Español");

    final String language;

    Language(String language){
        this.language = language;
    }

    public String getLanguage(){
        return language;
    }
}
