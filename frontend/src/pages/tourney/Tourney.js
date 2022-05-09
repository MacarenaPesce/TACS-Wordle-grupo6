import React, {Component} from 'react'
import NavbarAut from '../../components/navbar/Navbar';
import Footer from '../../components/footer/Footer';
import './Tourney.css'
import ComponenteTabs from './ComponenteTabs';
import Not from "../../components/not/Not";
import AuthService from "../../service/AuthService";


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


