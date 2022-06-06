import React, {Component} from 'react'
import NavbarAut from '../../components/navbar/Navbar';
import Footer from '../../components/footer/Footer';
import './Tourney.css'
import ComponenteTabs from './ComponenteTabs';
import Not from "../../components/not/Not";
import AuthService from "../../service/AuthService";
import StatusCheck from "../sesion/StatusCheck";
import Handler from "../sesion/Handler";


export default class Tourney extends Component{

    constructor(){
        super()
        this.state = {
            logueado: false,
            errorMessage: '',
            sessionError: false
        }
    }

    componentDidMount() {
        if (localStorage.getItem('token')) {
            this.setState({logueado: true});
        }
        console.log('Ping del token en el store...')
        AuthService.ping()
            .then(response => {
                console.log('Response del ping: ' + response.status)
            })
            .catch(error => {
                console.log(error)
                Handler.handleSessionError(this, error)
            })

    }



    render() {
        return(
            <div className='tourney'>
                {this.state.sessionError &&
                    <Not message={this.state.errorMessage}/>}

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


