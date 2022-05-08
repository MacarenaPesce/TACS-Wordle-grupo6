import AuthService from "../../service/AuthService";


export default function SessionCheck(responseStatusCode, message) {

        if(responseStatusCode == 401){
            alert("Ha expirado la sesión. \n"+message)
            AuthService.logout()

        }
        if(responseStatusCode == 403){
            alert("JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted. \n\n"+message)
            AuthService.logout()
        }


}