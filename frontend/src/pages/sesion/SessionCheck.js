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
        //AuthService.logout()  tenemos code 400 para cualquier error de negocio. Deberia cerrarse la sesión, solo si se llega a una bad request a causa de campos erróneos en el store.
        return message

    }

    alert("Error de conexión desconocido - status "+responseStatusCode+"\n"+message)
    return "Error desconocido"

}