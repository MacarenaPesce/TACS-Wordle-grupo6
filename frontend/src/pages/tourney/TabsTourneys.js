import React from 'react';
import Tab from 'react-bootstrap/Tab';
import { BsTrashFill, BsInfoLg, BsCheckLg } from "react-icons/bs";

const TabsTourneys = () => {

    return(
        <div className="col-md-12 search-table-col">
                            <div className="col-lg-3 col-md-offset-3"> {/* todo: esta columna tiene que ir a la derecha */}
                                <input className="search form-control" type="text" placeholder="Search by typing here.." />
                            </div>

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
    )

}

export default TabsTourneys;