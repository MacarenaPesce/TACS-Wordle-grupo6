package utn.frba.wordle.model.enums;

import java.util.Locale;

public enum ErrorMessages {
    RESULTS_ALREADY_SUBMITTED("El usuario ya ha publicado sus resultados en el día."),
    TOURNAMENT_WITH_SAME_NAME_EXISTS ("Ya existe un torneo con este nombre."),
    INVALID_RESULT_VALUE("Solo se pueden cargar resultados del 1 al 7."),
    MAIL_IN_USE("El mail ingresado ya se ecuentra en uso."),
    TOURNAMENT_DONT_EXISTS("El torneo especificado no existe."),
    YOU_ONLY_CAN_ADD_MEMBERS_TO_YOUR_TOURNAMENTS("Solo puedes agregar miembros a un torneo que tu hayas creado."),
    USER_DONT_EXISTS("El usuario especificado no se encuentra registrado en el sistema."),
    INVALID_USER_AND_PASSWORD("Combinación de usuario y contraseña inválidos."),
    USER_ALREADY_JOINED_TOURNAMENT("El usuario ya se encuentra unido al torneo."),
    CANNOT_JOIN_PRIVATE_TOURNAMENT("Sólo puedes ingresar a un torneo privado si el dueño te agrega."),
    USER_ALREADY_MEMBER_OF_TOURNAMENT("El usuario %s ya se encuentra agregado al torneo %s"),
    INCORRECT_SOLUTION_LENGHT("La solución %s no puede tener la longitud %s"),
    USER_IS_NOT_MEMBER_OF_TOURNAMENT("El usuario no es miembro del torneo solicitado");
    private final String description;

    ErrorMessages(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
