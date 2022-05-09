import AuthService from "../../service/AuthService";


export default function SessionCheck(responseStatusCode, message) {

    if(responseStatusCode === "401"){
        AuthService.logout()
        return "Ha expirado la sesión. \n"+message

    }
    if(responseStatusCode === "403"){
        AuthService.logout()
        return "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted. \n\n"+message

    }

    if(responseStatusCode === "400"){
        AuthService.logout()
        return "Se borraron/modificaron campos críticos del store (o alguna otra causa de 400 bad request) \n\n"+message

    }

    alert("Error de conexión desconocido - status "+responseStatusCode+"\n"+message)
    return "Error desconocido"

}