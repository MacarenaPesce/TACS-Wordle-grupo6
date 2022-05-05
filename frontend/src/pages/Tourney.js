import React, { Component } from 'react';
import NavbarAut from '../components/navbar/Navbar';
import Footer from '../components/footer/Footer';
import './Tourney.css'
import Tabs from 'react-bootstrap/Tabs';
import Tab from 'react-bootstrap/Tab';
import { BsTrashFill, BsInfoLg, BsCheckLg } from "react-icons/bs";
import UserService from "../service/UserService";
import TourneyCreate from './TourneyCreate'

export default class Tourney extends Component{

    constructor(){
        super()
        this.state = {
            myTourneys: []
        }
    }

    submitHandler = e => {
        e.preventDefault()
        console.log('Boton presionado')
        UserService.getMyTourneys()
            .then(response => {
                console.log('Response obtenida: ')
                console.log(response.data)
                this.setState({myTourneys: response.data.tourneys})
            })
            .catch(error => {
                console.log(error)
            })
    }



    render(){
        const prueba = [
            { tourneyId: 1, name: "bread", language: 50, type: "cupboard", state: 50, start: "cupboard", finish: 50, owner: "cupboard" },
            { tourneyId: 2, name: "name", language: "lang", type: "tipo", state: "estado", start: "inicio", finish: "fin", owner: "dueño" },
            { tourneyId: 3, name: "pan", language: "ES", type: "PUBLIC", state: "ACTIVE", start: "hoy", finish: "mañana", owner: "pepito" },
            { tourneyId: 4, name: "bread", language: 50, type: "cupboard", state: 50, start: "cupboard", finish: 50, owner: "cupboard" }
        ];

        return(
            <div className='tourney'> {/* todo: ver si es div o body  */}
                <header className='NavTourney'>
                    <NavbarAut />
                </header>
                <Tabs defaultActiveKey="myTourney" id="uncontrolled-tab-example" className="mb-3">
                    <Tab eventKey="myTourney" title="Mis torneos" >
                        {/*<p>Mis torneos</p>*/}
                        <TourneyCreate />
                        <div className="col-md-12 search-table-col">
                            <nav className="navbar navbar-light navbar-expand">
                                <form className="form-inline navbar-nav" onSubmit={this.submitHandler}>
                                    <input className="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search"></input>
                                    <button className="btn btn-outline-success my-2 my-sm-0" type="submit">Actualizar/Buscar</button>
                                </form>
                            </nav>
                            {/*<span className="counter pull-right"></span>*/}

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
                                        {/*<tr className="warning no-result">
                                            <td colSpan="12">
                                                <i className="fa fa-warning"></i>
                                                No result !!
                                            </td>
                                        </tr>*/}
                                        {this.state.myTourneys.map((tourney) => (
                                            <tr key={tourney.tourneyId}>
                                                <td>{tourney.tourneyId}</td>
                                                <td>{tourney.name}</td>
                                                <td>{tourney.type}</td>
                                                <td>{tourney.language}</td>
                                                <td>{tourney.start}</td>
                                                <td>{tourney.finish}</td>
                                                <td>{tourney.owner.username}</td>
                                                <td>
                                                    <button className="btn btn-success"  type="submit">
                                                        {/*<i className="fa fa-check" ></i>*/}
                                                        <BsCheckLg />
                                                    </button>
                                                    <button className="btn btn-danger"  type="submit">
                                                        {/*<i className="fa fa-trash" ></i>*/}
                                                        <BsTrashFill />
                                                    </button>
                                                    <button className="btn btn-primary"  type="submit">
                                                        {/*<i className="fa fa-trash" ></i>*/}
                                                        <BsInfoLg /> {/*en info puede ir el creador, el puntaje, el puesto */}
                                                    </button>
                                                </td>
                                            </tr>
                                        ))}
                                        <tr>
                                            <td> 01</td>
                                            <td> 01</td>
                                            <td> 01</td>
                                            <td> 01</td>
                                            <td> 01</td>
                                            <td> 01</td>
                                            <td> 01</td>
                                            <td> 
                                                <button className="btn btn-success"  type="submit">
                                                    {/*<i className="fa fa-check" ></i>*/}
                                                    <BsCheckLg />
                                                </button>
                                                <button className="btn btn-danger"  type="submit">
                                                    {/*<i className="fa fa-trash" ></i>*/}
                                                    <BsTrashFill />
                                                </button>
                                                <button className="btn btn-primary"  type="submit">
                                                    {/*<i className="fa fa-trash" ></i>*/}
                                                    <BsInfoLg /> {/*en info puede ir el creador, el puntaje, el puesto */}
                                                </button>
                                            </td>
                                        </tr>
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
                                    </tfoot>*/}
                                </table>
                            </div>
                        </div>
                        
                    </Tab>
                    <Tab eventKey="publicTourney" title="Publicos">
                        <p>Torneos publicos</p>
                    </Tab>
                    <Tab eventKey="finishTourney" title="Finalizados" >
                        <p>Torneos finalizados</p>
                    </Tab>
                </Tabs>

                <Footer />
            </div>
        );
    }
}