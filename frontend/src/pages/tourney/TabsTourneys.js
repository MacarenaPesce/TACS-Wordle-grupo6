import React, {Component, useState} from 'react'
import Tab from 'react-bootstrap/Tab';
import UserService from "./../../service/UserService"
import TourneyCreate from './TourneyCreate'
import './Tourney.css'
import SessionCheck from "../sesion/SessionCheck";
import BotonesTorneos from './BotonesTorneos.js'
import Not from "../../components/not/Not";

export default class TabsTourneys extends Component{ 

    constructor(props){
        super(props)
        this.state = {
            myTourneys: [],
            sessionError: false,
            errorMessage: ''
        }
    }
    
    submitHandler = e => {
        e.preventDefault()
        console.log('mostrando torneos')
        this.submitTourneys()
    }

    submitTourneys() {
        console.log("submit tourneys")
        UserService.getMyTourneys(this.props.nombreTabla) /*todo: como lo mando si no recibe parametros ._. mandar aca el tipo de torneos */ //mis torneos es el nombre del metodo, para otra tabla es otro metodo
            .then(response => {
                console.log('Response obtenida: ')
                console.log(response.data)
                this.setState({myTourneys: response.data.tourneys})
                if(JSON.stringify(this.state.myTourneys[0]) === undefined){
                    //todo mostrar mensaje de tabla vacia
                }
            })
            .catch(error => {
                console.log(error)
                const status = JSON.stringify(error.response.status)
                const message = SessionCheck(status,JSON.stringify(error.response.data.message));
                if(status === "401" || status === "403"){   //para este botón se podría incluir el 400, como no es un form. La unica causa de 400 puede ser que el store de la sesión esté corrupto. Dónde poner el codigo de log out?
                    this.setState({sessionError: true, errorMessage: message})
                }
            })
    }

    render() {
      
        let listTourneys = this.state.myTourneys.map((tourney) =>
                    <tr key={tourney.tourneyId}>
                        <td> {tourney.tourneyId}</td>
                        <td> {tourney.name}</td>
                        <td> {tourney.type}</td>
                        <td> {tourney.language}</td>
                        <td> {tourney.start}</td>
                        <td> {tourney.finish}</td>
                        <td> {tourney.owner.username}</td>

                        <td>
                            <BotonesTorneos 
                                torneo= {tourney}
                            />   
                        </td>
                    </tr>
                );

        return (
            <div className="col-md-12 search-table-col">

                {this.state.sessionError &&
                    <Not message={this.state.errorMessage}/>}

                {/*------------------------------------------------------------------ */}
                {/*todo: sacar este container y habilitar el TabIntro.js*/}
                <div className="container">
                    <div className="row">
                        <div className="col-md-3">
                            <form className="form-inline">
                                <input className="form-control " type="search" placeholder="Ingrese nombre del torneo"
                                       aria-label="Search"/>
                            </form>
                        </div>
                        <div className="col-md-3">
                            <form className="form-inline" >
                                <button className="btn btn-outline-success my-2 my-sm-0"
                                        type="submit">Buscar
                                </button>
                            </form>
                        </div>
                        <div className="col-md-3">
                            <form className="form-inline" onSubmit={this.submitHandler}>
                                <button className="btn btn-outline-success my-2 my-sm-0"
                                        type="submit">Actualizar
                                </button>
                            </form>
                        </div>
                        <div className="col-md-1"> {/*sirve para que el btn de crear torneo este a la derecha */}

                        </div>
                        <div className="col-md-2"> 
                            <TourneyCreate/>
                        </div>
                    </div>
                </div>      
                {/*------------------------------------------------------------------ */}

                <div className="table-responsive table table-hover table-bordered results">
                    <table className="table table-hover table-bordered">
                        <thead className="bill-header cs">
                        <tr>
                            <th id="trs-hd-1" className="col-lg-1"> N°</th>
                            <th id="trs-hd-2" className="col-lg-2"> Nombre</th>
                            <th id="trs-hd-3" className="col-lg-1"> Tipo</th>
                            <th id="trs-hd-4" className="col-lg-1"> Lenguaje</th>
                            <th id="trs-hd-5" className="col-lg-1"> Inicio</th>
                            <th id="trs-hd-6" className="col-lg-1"> Fin</th>
                            <th id="trs-hd-7" className="col-lg-2"> Creador</th>
                            <th id="trs-hd-8" className="col-lg-1"> Acciones</th>
                            {/** todo: las dos col de abajo NO tienen que aparecer en tabla de publicos */}
                            <th id="trs-hd-7" className="col-lg-1"> Puntaje</th>
                            <th id="trs-hd-7" className="col-lg-1"> Posicion</th>
                        </tr>
                        </thead>

                        {<tbody>
                        {listTourneys}
                        </tbody>}

                    </table>
                </div>

                {/*------------------------------------------------------------------ */}
            </div>
        );
    }
}





                        
