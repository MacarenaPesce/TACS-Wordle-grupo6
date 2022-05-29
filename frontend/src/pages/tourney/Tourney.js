import React, {Component} from 'react'
import NavbarAut from '../../components/navbar/Navbar';
import Footer from '../../components/footer/Footer';
import './Tourney.css'
import ComponenteTabs from './ComponenteTabs';
import Not from "../../components/not/Not";
import AuthService from "../../service/AuthService";
import StatusCheck from "../sesion/StatusCheck";


export default class Tourney extends Component{

    constructor(){
        super()
        this.state = {
            logueado: false,
            missingCredentials: false
        }
    }

    componentDidMount(){ //todo analizar si se prefiere cambiar por componentDidUpdate() / useEffect()
        //todo mirar si es necesario hacer una llamada a la api, para verificar que el logueo sea válido
        if(localStorage.getItem('token')){
            this.setState({logueado: true});
        } else {
            this.setState({missingCredentials: true});
            AuthService.logout();
        }
    }

    static handleSessionError(component, error){
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

    render() {
        return(
            <div className='tourney'>
                {this.state.missingCredentials &&
                    <Not message="Olvidó traer sus credenciales."/>}

                {this.state.logueado ? (
                    <React.Fragment>
                        <header className='NavTourney'>
                            <NavbarAut />
                        </header>

                        <ComponenteTabs />

                        <Footer />
                    </React.Fragment>
                    ) : (
                       <div> Cargando!! ∞ ∞ ∞ ∞ ∞ </div>
                    )
                }
            </div>
        );
    }


}


