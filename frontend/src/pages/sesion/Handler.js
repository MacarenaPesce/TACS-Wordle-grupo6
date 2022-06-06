import AuthService from "../../service/AuthService";
import StatusCheck from "./StatusCheck";

/**
 * Verificar los errores que requieren cerrar sesión. En ese caso, cerrar la sesión.
 * Solo se puede llamar desde class.
 * La class debe tener los estados sessionError y errorMessage creados. Y debe incluir la siguiente linea en el return del render:
 *               {this.state.sessionError && <Not message={this.state.errorMessage}/>}
 * @param component comoponente con esas dos variables creadas, para mandar como 'this'
 * @param error error atrapado por el catch de una request de axios
 */
const handleSessionError = (component, error) => {
    if(error.response === undefined){
        AuthService.logout()
        component.setState({sessionError: true, errorMessage: "No hay conexión con el back"})
        return;
    }

    const status = JSON.stringify(error.response.status)
    const message = StatusCheck(status,JSON.stringify(error.response.data.message));
    if(status === "401" || status === "403"){
        AuthService.logout()
        component.setState({sessionError: true, errorMessage: message})
    }
}

/**
 * Verificar los errores que requieren cerrar sesión. En ese caso, cerrar la sesión.
 * Solo se puede llamar desde una función de React.
 * El componente de función debe tener los estados sessionError y errorMessage creados. Y debe incluir la siguiente linea en el render:
 *               {sessionError && <Not message={errorMessage}/>}
 * @param setSessionError funcion para actualizar el estado en el componente de funcion
 * @param setErrorMessage funcion para actualizar el estado en el componente de funcion
 * @param error error atrapado por el catch de una request de axios
 */
const handleSessionErrorFunc = (setSessionError, setErrorMessage, error) => {
    if(error.response === undefined){
        AuthService.logout()
        setSessionError(true)
        setErrorMessage("No hay conexión con el back")
        return;
    }

    const status = JSON.stringify(error.response.status)
    const message = StatusCheck(status,JSON.stringify(error.response.data.message));
    if(status === "401" || status === "403"){
        AuthService.logout()
        setSessionError(true)
        setErrorMessage(message)
    }
}

export default {
    handleSessionError,
    handleSessionErrorFunc
};
