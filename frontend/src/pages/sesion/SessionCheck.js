import AuthService from "../../service/AuthService";


export default function SessionCheck(responseStatusCode, message) {

        AuthService.logout()

        if(responseStatusCode === "401"){

            return "Ha expirado la sesión. \n"+message

        }
        if(responseStatusCode === "403"){

            return "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted. \n\n"+message

        }

        //400
        return "Se borraron/modificaron campos críticos del store"

}