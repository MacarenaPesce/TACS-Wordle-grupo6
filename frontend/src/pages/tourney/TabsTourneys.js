import React, {useState} from 'react'
import Tab from 'react-bootstrap/Tab';
import { BsTrashFill, BsInfoLg, BsCheckLg } from "react-icons/bs";
import UserService from "./../../service/UserService"
import TourneyCreate from './TourneyCreate'


const TabsTourneys = ({nombreTabla}) => {

    const tourneys= []; 

    const handleSubmit = (e) => {
        e.preventDefault()
        console.log('mostrando torneos')
        
        UserService.getMyTourneys() /*mandar aca el tipo de torneos */
            .then(response => {
                console.log('Response obtenida: ')
                console.log(response.data)
                tourneys.setState({tourneys: response.data.tourneys})
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
            <td> {tourney.owner.username}</td> {/*el creador tiene que ser un usuario, lo pongo aca o en info?*/}

            <td> 
                <button className="btn btn-success"  type="submit">
                    <BsCheckLg />
                </button>
                <button className="btn btn-danger"  type="submit">
                    <BsTrashFill />
                </button>
                <button className="btn btn-primary"  type="submit">
                    <BsInfoLg /> {/*en info puede ir el creador, el puntaje, el puesto */}
                </button>
            </td>
        </tr>
    );

    return(
        <div className="col-md-12 search-table-col">
                            <div className="col-lg-3 col-md-offset-3"> {/* todo: esta columna tiene que ir a la derecha */}
                                <input className="search form-control" type="text" placeholder="Search by typing here.." />
                            </div>

                            <TourneyCreate />

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
                                    </tfoot>*/}
                                </table>
                            </div>
                        </div>
    )

}

export default TabsTourneys;