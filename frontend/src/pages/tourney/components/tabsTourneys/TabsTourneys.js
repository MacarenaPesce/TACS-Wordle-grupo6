import React, {Component, useState} from 'react'
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
            currentPage: 1,
            maxResults: 4,
            totalPages: 100,
            sessionError: false,
            errorMessage: '',
            name:'',
            loading: false,
            error: false
        }
    }

    nextPage = () => {
        //console.log("page actual:", this.state.currentPage);
       // console.log(this.state.name);
        //console.log(this.state.myTourneys.filter( torneo => torneo.name.includes( this.state.name ) ).length);
       // if ( this.state.myTourneys.filter( torneo => torneo.name.includes( this.state.name ) ).length >= this.state.currentPage + 1 ){
          if( this.state.currentPage < this.state.totalPages){
            let page = this.state.currentPage + 1;
            this.setState({currentPage: page});
            this.submitTourneys(page);
        }
        //console.log("accion: nextPage, ","page:",this.state.currentPage, );
    }

    prevPage = () => {
        //console.log("page actual:", this.state.currentPage);

        if( this.state.currentPage > 1){
            let page = this.state.currentPage - 1;
            this.setState({currentPage: page});
            this.submitTourneys(page);
        }  
        //console.log("accion: prevpage, ","page:",this.state.currentPage);
    }

    componentDidMount() {
        this.submitTourneys(1)
    }

    /*
    componentDidUpdate() {
        this.submitTourneys(1)
    }*/
    
    submitHandler = e => {
        e.preventDefault()
        this.submitTourneys(1)
    }

    submitTourneys(page) {
        console.log("se pide actualizar la lista de torneos por pagina, con pag= ", page);
        console.log("con nombre tabla: ", this.props.nombreTabla)
        console.log("name:=", this.state.name)
        this.setState({loading: true, error: false})
         UserService.getMyTourneys(this.props.nombreTabla, page,this.state.maxResults, this.state.name)
            .then(response =>  { 
                this.setState({myTourneys: response.data.tournaments});
                this.setState({totalPages: response.data.totalPages})
                console.log("respuesta del server: ",response.data.tournaments)
                if(JSON.stringify(this.state.myTourneys[0]) === undefined){
                    //todo: mostrar mensaje de tabla vacia
                }
                this.setState({loading: false})
            })
            .catch(error => {

                Handler.handleSessionError(this, error)
                this.setState({loading: false, error: true})
            })
    }

    filtro =(e) =>{
        e.preventDefault()
        this.setState({currentPage: 1}); //mando a la primera pagina
        this.setState({name:e.target.value});
        console.log("name desde filtro= ", this.state.name);
        this.submitTourneys(this.state.currentPage);
    }

    formatDate(start) {
        let fecha = new Date(start).toGMTString();
        return fecha.substring(0,17);
    }

    render() {
        let listTourneys = []
      if(this.state.myTourneys.length >0){
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
                        <div className="col-md-3">
                            <form className="form-inline">
                                <input className="form-control " type="search" 
                                       placeholder="Ingrese nombre del torneo"
                                       value={this.state.name}
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
                        <div className="col-md-2">
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

                <button className='btn btn-primary' onClick={this.prevPage}>
                    Anterior
                </button>
                &nbsp;
                <button className='btn btn-primary' onClick={this.nextPage}> 
                    Siguiente
                </button>

                {/*------------------------------------------------------------------ */}
            </div>
        );
    }
}
