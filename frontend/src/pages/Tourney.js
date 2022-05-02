import React, { Component } from 'react';
import NavbarAut from '../components/navbar/Navbar';
import Footer from '../components/footer/Footer';
import './Tourney.css'
import Tabs from 'react-bootstrap/Tabs';
import Tab from 'react-bootstrap/Tab';
import { BsTrashFill, BsInfoLg, BsCheckLg } from "react-icons/bs";

export default class Tourney extends Component{

    constructor(){
        super()
        this.state = {
            name: '',
            language:'',
            type:'',
            start:'',
            finish:'',
            owner:''
        }
    }


    render(){

        return(
            <body className='tourney'>
                <header className='NavTourney'>
                    <NavbarAut />
                </header>
                {/*
                <div>
                    <ul class="nav nav-tabs" role="tablist">
                        <li class="nav-item" role="presentation">
                            <a class="nav-link active" role="tab" data-bs-toggle="tab" href="#tabMyTour"> Mis Torneos</a>
                        </li>
                        <li class="nav-item" role="presentation">
                            <a class="nav-link" role="tab" data-bs-toggle="tab" href="#tabPublicTour"> Publicos</a>
                        </li>
                        <li class="nav-item" role="presentation">
                            <a class="nav-link" role="tab" data-bs-toggle="tab" href="#tabFinTour"> Finalizados</a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div id="tabMyTour" class="tab-pane active" role="tabpanel">
                            <p> Contenido de mis torneos</p>
                        </div>
                        <div id="tabPublicTour" class="tab-pane active" role="tabpanel">
                            <p> Contenido de torneos publicos</p>
                        </div>
                        <div id="tabFinTour" class="tab-pane active" role="tabpanel">
                            <p> Contenido de torneos finalizados</p>
                        </div>
                    </div>
                </div>
                */}



                <Tabs defaultActiveKey="myTourney" id="uncontrolled-tab-example" className="mb-3">
                    <Tab eventKey="myTourney" title="Mis torneos">
                        {/*<p>Mis torneos</p>*/}
                        <div class="col-md-12 search-table-col">
                            <div class="col-lg-3 col-md-offset-3"> {/* todo: esta columna tiene que ir a la derecha */}
                                <input class="search form-control" type="text" placeholder="Search by typing here.." />
                            </div>

                            {/*<span class="counter pull-right"></span>*/}

                            <div class="table-responsive table table-hover table-bordered results">
                                <table class="table table-hover table-bordered">
                                    <thead class="bill-header cs">
                                        <tr>
                                            <th id="trs-hd-1" class="col-lg-1"> N°</th>
                                            <th id="trs-hd-2" class="col-lg-2"> Nombre</th>
                                            <th id="trs-hd-3" class="col-lg-1"> Tipo</th>
                                            <th id="trs-hd-4" class="col-lg-2"> Lenguaje</th>
                                            <th id="trs-hd-5" class="col-lg-1"> Inicio</th>
                                            <th id="trs-hd-6" class="col-lg-1"> Fin</th>
                                            <th id="trs-hd-7" class="col-lg-2"> Creador</th>
                                            <th id="trs-hd-8" class="col-lg-2"> Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {/*<tr class="warning no-result">
                                            <td colSpan="12">
                                                <i class="fa fa-warning"></i>
                                                No result !!
                                            </td>
                                        </tr>*/}
                                        <tr>
                                            <td> 01</td>
                                            <td> 01</td>
                                            <td> 01</td>
                                            <td> 01</td>
                                            <td> 01</td>
                                            <td> 01</td>
                                            <td> 01</td>
                                            <td> 
                                                <button class="btn btn-success"  type="submit">
                                                    {/*<i class="fa fa-check" ></i>*/}
                                                    <BsCheckLg />
                                                </button>
                                                <button class="btn btn-danger"  type="submit">
                                                    {/*<i class="fa fa-trash" ></i>*/}
                                                    <BsTrashFill />
                                                </button>
                                                <button class="btn btn-primary"  type="submit">
                                                    {/*<i class="fa fa-trash" ></i>*/}
                                                    <BsInfoLg /> {/*en info puede ir el creador, el puntaje, el puesto */}
                                                </button>
                                            </td>
                                        </tr>
                                    </tbody>
                                    {/*}
                                    <tfoot>
                                        <tr>
                                            <td colSpan="1">
                                                <div class="paging">
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
            </body>
        );
    }
}