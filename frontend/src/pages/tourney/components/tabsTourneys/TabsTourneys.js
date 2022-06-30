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
            currentPage: 1,
            maxResults: 4,
            sessionError: false,
            errorMessage: '',
            name:'',
            loading: false,
            error: false
        }
    }

    nextPage = () => {
        //console.log("page actual:", this.state.currentPage);
        if ( this.state.myTourneys.filter( torneo => torneo.name.includes( this.state.name ) ).length >= this.state.currentPage + 1 ){
            let page = this.state.currentPage + 1;
            this.setState({currentPage: page});
            this.submitTourneysPage(page);
        }
        //console.log("accion: nextPage, ","page:",this.state.currentPage, );
    }

    prevPage = () => {
        //console.log("page actual:", this.state.currentPage);

        if( this.state.currentPage > 1){
            let page = this.state.currentPage - 1;
            this.setState({currentPage: page});
            this.submitTourneysPage(page);
        }  
        //console.log("accion: prevpage, ","page:",this.state.currentPage);
    }

    componentDidMount() {
        this.submitTourneysPage(1)
    }

    /*
    componentDidUpdate() {
        this.submitTourneysPage(1)
    }*/
    
    submitHandler = e => {
        e.preventDefault()
        this.submitTourneys()
    }

    submitTourneys() {
        console.log("se pide actualizar la lista de torneos por pagina")
        this.setState({loading: true, error: false})
        UserService.getMyTourneys(this.props.nombreTabla, this.state.currentPage,this.state.maxResults) //mis torneos es el nombre del metodo, para otra tabla es otro metodo
            .then(response => {
                this.setState({myTourneys: response.data.tournaments});
                
                if(JSON.stringify(this.state.myTourneys[0]) === undefined){
                    //todo: mostrar mensaje de tabla vacia
                }
                this.setState({loading: false})
            })
            .catch(error => {
                Handler.handleSessionError(this.state.sessionError, this.state.errorMessage, error)
                this.setState({loading: false, error: true})
            })
    }

    //duplico esta parte para que se actualice la lista de torneos cuando toco un boton de paginacion
    submitTourneysPage(page){
        this.setState({loading: true, error: false})
        UserService.getMyTourneys(this.props.nombreTabla, page,this.state.maxResults)
            .then(response => {
                this.setState({myTourneys: response.data.tournaments});
                
                if(JSON.stringify(this.state.myTourneys[0]) === undefined){
                    //todo: mostrar mensaje de tabla vacia
                }
                this.setState({loading: false})
            })
            .catch(error => {
                Handler.handleSessionError(this.state.sessionError, this.state.errorMessage, error);
                this.setState({loading: false, error: true})
            })        
    }

    filtro =(e) =>{
        const keyword = e.target.value;

        this.setState({currentPage: 1}); //mando a la primera pagina

        if(keyword !==''){
            const result = this.state.myTourneys.filter((tourney) =>{  //aca deberia ser de todos los torneos, mytourneys solo tiene los de dicha pagina
                return tourney.name.toLowerCase().startsWith(keyword.toLowerCase());
            });
            this.setState({myTourneys: result});                        //aca deberia setear la pagina???
        } else {
            this.submitTourneys();  //todo modificar esto , TIENE QUE USAR EL GET TOURNEY GENERICO ??? 
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
                        <div className="col-md-3">
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
