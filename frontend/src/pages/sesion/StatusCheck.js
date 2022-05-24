
export default function StatusCheck(responseStatusCode, message) {

    if(responseStatusCode === "401"){
        return "Ha expirado la sesión. \n"+message

    }
    if(responseStatusCode === "403"){
        return "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted. \n\n"+message

    }

    if(responseStatusCode === "400"){
        //  tenemos code 400 para cualquier error de negocio.
        return "Bad request / BusinessException. \n"+message

    }

    alert("Error de conexión desconocido - status "+responseStatusCode+"\n"+message)
    return "Error desconocido"

}