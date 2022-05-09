import React, {Component, useState} from 'react'
import Tab from 'react-bootstrap/Tab';

import UserService from "./../../service/UserService"
import TourneyCreate from './TourneyCreate'
import './Tourney.css'
import SessionCheck from "../sesion/SessionCheck";
import BotonesTorneos from './BotonesTorneos.js'

/*
const TabsTourneys = ({nombreTabla}) => {

    constructor(){
        super()
        this.state = {
            myTourneys: []
        }
    }

    submitHandler = e => {
        e.preventDefault()
        console.log('mostrando torneos')       
        UserService.getMyTourneys() /*mandar aca el tipo de torneos 
            .then(response => {
                console.log('Response obtenida: ')
                console.log(response.data)
                this.setState({myTourneys: response.data.tourneys})
            })
            .catch(error => {
                console.log(error)
            })
    }

    let listTourneys = tourneys.map((tourney) =>
        <tr>
            <td> {tourney.id}</td>
            <td> {tourney.name}</td>
            <td> {tourney.type}</td>
            <td> {tourney.lenguage}</td>
            <td> {tourney.start}</td>
            <td> {tourney.finish}</td>
            <td> {tourney.owner.username}</td>

            <td> 
                <button className="btn btn-success"  type="submit">
                    <BsCheckLg />
                </button>
                <button className="btn btn-danger"  type="submit">
                    <BsTrashFill />
                </button>
                <button className="btn btn-primary"  type="submit">
                    <BsInfoLg />
                </button>
            </td>
        </tr>
    );

    return(
        <div className="col-md-12 search-table-col">
                   




                            <div className="table-responsive table table-hover table-bordered results">
                                <table className="table table-hover table-bordered">
                                    <thead className="bill-header cs">
                                        <tr>
                                            <th id="trs-hd-1" className="col-lg-1"> N°</th>
                                            <th id="trs-hd-2" className="col-lg-2"> Nombre</th>
                                            <th id="trs-hd-3" className="col-lg-1"> Tipo</th>
                                            <th id="trs-hd-4" className="col-lg-2"> Lenguaje</th>
                                            <th id="trs-hd-5" className="col-lg-1"> Inicio</th>
                                            <th id="trs-hd-6" className="col-lg-1"> Fin</th>
                                            <th id="trs-hd-7" className="col-lg-2"> Creador</th>
                                            <th id="trs-hd-8" className="col-lg-2"> Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {listTourneys}
                                    </tbody>
                                    {/*}

                                    <tfoot>
                                        <tr>
                                            <td colSpan="1">
                                                <div className="paging">
                                                    <ul>
                                                        <li> <a href="#"> <span> Anterior </span> </a> </li>
                                                        <li> <a href="#"> <span> 1 </span> </a> </li>
                                                        <li> <a href="#"> <span> 2 </span> </a> </li>
                                                        <li> <a href="#"> <span> 3 </span> </a> </li>
                                                        <li> <a href="#"> <span> Siguiente </span> </a> </li>
                                                    </ul>
                                                </div>
                                            </td>
                                        </tr>
                                    </tfoot>
                    </table>
                </div>
            </div>
        );
    }
}

export default TabsTourneys;*/


export default class TabsTourneys extends Component{ 

    constructor(props){
        super(props)
        this.state = {
            myTourneys: []
        }
    }

    componentDidMount() {
        /*debugger
        console.log("did mount")*/
        if(this.props.nombreTabla === 'Mis torneos'){
            /*console.log("estas en mis torneos")*/
            this.submitTourneys()
        }
        else{
            /*console.log("no estas en mis torneos")*/
        }
    }

    componentDidUpdate() {
        /*this.submitTourneys()*/
        /*console.log("did update")*/
    }
    
    submitHandler = e => {
        e.preventDefault()
        console.log('mostrando torneos')
        this.submitTourneys()
    }

    submitTourneys() {
        console.log("submit tourneys")
        UserService.getMyTourneys(this.props.nombreTabla) /*todo: como lo mando si no recibe parametros ._. mandar aca el tipo de torneos */
            .then(response => {
                console.log('Response obtenida: ')
                console.log(response.data)
                this.setState({myTourneys: response.data.tourneys})
            })
            .catch(error => {
                console.log(error)
                SessionCheck(JSON.stringify(error.response.status),JSON.stringify(error.response.data.message));
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
                                type= {tourney.type}
                            />   
                        </td>
                    </tr>
                );

        return (
            <div className="col-md-12 search-table-col">

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
                            <th id="trs-hd-8" className="col-lg-3"> Acciones</th>
                        </tr>
                        </thead>

                        {/*------------------------------------------------------------------ */}

                        {<tbody>
                          {listTourneys}
                        </tbody>}
                    </table>
                </div>
            </div>
        );
    }
}





                        
