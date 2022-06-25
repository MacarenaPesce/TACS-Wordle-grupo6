import React, {Component, useState} from 'react'
import Tab from 'react-bootstrap/Tab';
import UserService from "../../../../service/UserService"
import TourneyCreate from './../createTourney/TourneyCreate'
import './../../Tourney.css'
import BotonesTorneos from './BotonesTorneos.js'
import Not from "../../../../components/not/Not";
import TourneySubmit from "../../components/submitResults/TourneySubmit";
import Handler from "../../../sesion/Handler";

export default class TabsTourneys extends Component{ 

    constructor(props){
        super(props)
        this.state = {
            myTourneys: [],
            sessionError: false,
            errorMessage: '',
            name:'',
            loading: false,
            error: false
        }
    }

    componentDidMount() {
        this.submitTourneys()
    }

    /*componentDidUpdate() {
        console.log("did update")
    }*/
    
    submitHandler = e => {
        e.preventDefault()
        this.submitTourneys()
    }

    submitTourneys() {
        console.log("se pide actualizar la lista de torneos")
        this.setState({loading: true, error: false})
        UserService.getMyTourneys(this.props.nombreTabla) //mis torneos es el nombre del metodo, para otra tabla es otro metodo
            .then(response => {
                if(this.props.nombreTabla == 'Publicos'){
                    let tourneysResponse = response.data.tournaments.filter(torneo => torneo.state == 'READY');
                    this.setState({myTourneys: tourneysResponse});
                }
                else{
                    this.setState({myTourneys: response.data.tournaments});
                }

                if(JSON.stringify(this.state.myTourneys[0]) === undefined){
                    //todo: mostrar mensaje de tabla vacia
                }
                this.setState({loading: false})
            })
            .catch(error => {
                console.log(error)
                Handler.handleSessionError(this, error)
                this.setState({loading: false, error: true})
            })
    }

    filtro =(e) =>{
        const keyword = e.target.value;

        if(keyword !==''){
            const result = this.state.myTourneys.filter((tourney) =>{
                return tourney.name.toLowerCase().startsWith(keyword.toLowerCase());
            });
            this.setState({myTourneys: result});
        } else {
            this.submitTourneys();
        }
        this.setState({name:keyword});
    }

    formatDate(start) {
        let fecha = new Date(start)
        let day = fecha.getDate()+1 ;
        let month = (fecha.getMonth() +1)>10?(fecha.getMonth() +1) : '0'+(fecha.getMonth() +1)  ; 
        let year = fecha.getFullYear();
        return day+'/'+month + '/' + year ;
    }

    render() {
        let listTourneys = []
      if(this.state.myTourneys){
        listTourneys = this.state.myTourneys.map((tourney) =>
                <tr key={tourney.tourneyId}>
                    <td> {tourney.tourneyId}</td>
                    <td> {tourney.name}</td>
                    <td> {tourney.type}</td>
                    <td> {tourney.language}</td>
                    <td> {tourney.state}</td>
                    <td> {this.formatDate(tourney.start)}</td>
                    <td> {this.formatDate(tourney.finish)}</td>
                    <td> {tourney.owner.username}</td>
                    <td>
                        <BotonesTorneos tourney={tourney}/>   
                    </td>
                </tr>
            );
        }


        const tablaVacia = (
            <div className="alert alert-secondary" role="alert">
                ⚠️Tabla vacia ⚠️
            </div>
        )

        const spinner = (
            <div>
                <div className="spinner-border espiner3" role="status">
                    <span className="visually-hidden">Loading...</span>
                </div>
                <div className="spinner-grow espiner4" role="status">
                    <span className="visually-hidden">Loading...</span>
                </div>
            </div>
        );

        let display;
        if (this.state.loading) {
            display = spinner;
        } else {
            if(this.state.myTourneys.length > 0){
                display = "";
            }
            else{
                display = tablaVacia;
            }
        }

        if(this.state.error){
            display = <div className="alert alert-danger" role="alert">
                        ⚠️Error catcheado ⚠️
                      </div>
        }

        return (
            <div className="col-md-12 search-table-col">

                {this.state.sessionError &&
                    <Not message={this.state.errorMessage}/>}

                {/*------------------------------------------------------------------ */}
                {/*todo: sacar este container y habilitar el TabIntro.js*/}
                <div className="container">
                    <div className="row">
                        <div className="col-md-2">
                            <form className="form-inline">
                                <input className="form-control " type="search" 
                                       placeholder="Ingrese nombre del torneo"
                                       value={this.name}
                                       onChange={this.filtro}
                                       aria-label="Search"/>
                            </form>
                        </div>

                        <div className="col-md-2">
                            <form className="form-inline" onSubmit={this.submitHandler}>
                                <button className="btn btn-outline-success my-2 my-sm-0"
                                        type="submit">Actualizar
                                </button>
                            </form>
                        </div>
                        <div className="col-md-3">
                            <TourneyCreate modal={true} min={this.props.min}/>
                        </div>
                        <div className="col-md-3">
                            <TourneySubmit modal={true}/>
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
                            <th id="trs-hd-8" className="col-lg-1"> Estado</th>
                            <th id="trs-hd-5" className="col-lg-2"> Inicio</th>
                            <th id="trs-hd-6" className="col-lg-2"> Fin</th>
                            <th id="trs-hd-7" className="col-lg-1"> Creador</th>
                            <th id="trs-hd-8" className="col-lg-1"> Acciones</th>
                        </tr>
                        </thead>

                        <tbody>
                            {listTourneys}
                        </tbody>

                    </table>
                    {display}

                </div>

                {/*------------------------------------------------------------------ */}
            </div>
        );
    }
}
