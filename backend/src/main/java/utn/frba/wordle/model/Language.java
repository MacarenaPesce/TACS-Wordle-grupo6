package utn.frba.wordle.model;

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
