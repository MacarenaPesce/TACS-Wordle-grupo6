import React, {useState} from 'react';
import Tabs from 'react-bootstrap/Tabs';
import Tab from 'react-bootstrap/Tab';
import TabsTourneys from './TabsTourneys';
import TabIntro from './TabIntro'
import {BsInfoLg, BsTrashFill} from "react-icons/bs";
import {AiOutlineUserAdd, AiOutlineUsergroupAdd, AiOutlineCrown} from "react-icons/ai";
import TourneyCreate from './TourneyCreate'
import {GoCheck} from "react-icons/go";


const ComponenteTabs = () => {

    const nombres = {
        misTorneos: 'Mis torneos', 
        torneosPublicos: 'Publicos', 
        finalizados: 'Finalizados',
    }

    const [mostrar, setMostrar] = useState(false);

    const ayudaMis = <div className="alert alert-primary" role="alert">
        <p>
            <h4><p>Te salen tus torneos creados:</p></h4>
            <button className="btn btn-warning" type="submit" >
                <AiOutlineUsergroupAdd/>
            </button> Boton para sumar personas
            <p></p>
            <button className="btn btn-primary" type="submit" href="/info">
                <BsInfoLg/>
            </button> Ranking del torneo
            <p></p>
            <button className="btn btn-danger" type="submit">
                <BsTrashFill/>
            </button> Finalizar torneo ya (extra)
            <p></p>

            <h4><p>Te salen torneos de otros a los que estas anotado:</p></h4>
            <button className="btn btn-primary" type="submit" href="/info">
                <BsInfoLg/>
            </button> Ranking del torneo
            <p></p>
            <button className="btn btn-danger" type="submit">
                <BsTrashFill/>
            </button> Salir del torneo (extra) (dejas de estar anotado)
            <p></p>
            <button className="btn btn-info" type="submit">
                <AiOutlineUserAdd/>
            </button> Este botón se está mostrando ahora pero no va
        </p>
    </div>

    const ayudaPublic =     <div className="alert alert-primary" role="alert">
        <p>
            <h4><p>Te salen torneos publicos de otros a los que te podes anotar:</p></h4>
            <button className="btn btn-info" type="submit">
                <AiOutlineUserAdd/>
            </button> Boton para sumarte (solo si aún no estás participando)
            <p></p>
            <button className="btn btn-success" type="submit">
                <GoCheck/>
            </button> Indicador si ya te sumaste
            <p></p>
            <button className="btn btn-primary" type="submit" href="/info">
                <BsInfoLg/>
            </button> Ranking del torneo
            <p></p>
            <button className="btn btn-danger" type="submit">
                <BsTrashFill/>
            </button> Salir del torneo (extra) (solo si estas participando)
            <p></p>

            <h4><p>Te salen tus torneos publicos creados:</p></h4>
            <button className="btn btn-warning" type="submit" >
                <AiOutlineUsergroupAdd/>
            </button> Boton para sumar personas
            <p></p>
            <button className="btn btn-primary" type="submit" href="/info">
                <BsInfoLg/>
            </button> Ranking del torneo
            <p></p>
            <button className="btn btn-danger" type="submit">
                <BsTrashFill/>
            </button> Finalizar torneo ya (extra)

        </p>
    </div>

    const ayudaFin = <div className="alert alert-primary" role="alert">
        <p>
            <h4><p>Torneos finalizados de los cuales hayas participado/jugado:</p></h4>
            <button className="btn btn-primary" type="submit" href="/info">
                <BsInfoLg/>
            </button> Ranking final del torneo (no difiere de todos los botones de ranking)
            <p></p>
            <button className="btn btn-warning" type="submit" href="/info">
                <AiOutlineCrown/>
            </button> Mostrar el ganador en una columna con este icono a su lado
        </p>
    </div>

    const botonAyuda =  <button className="btn btn-info dropdown-toggle" type="button" id="dropdownMenu2"
                            data-bs-toggle="dropdown" aria-expanded="false"
                            onClick={clickToggle}>
                            Ayuda  </button>

    function clickToggle(){
        if(mostrar === true)
            setMostrar(false)
        else
            setMostrar(true)
    }

    //TODO logica de volver a cargar / request de lista de torneos cada vez que cambias de tab
    return(
        <div>

        <Tabs defaultActiveKey="myTourney" id="uncontrolled-tab-example" className="mb-3">
            <Tab eventKey="myTourney" title={nombres.misTorneos}>
                {/*<TabIntro />*/}
                {botonAyuda}
                {mostrar && ayudaMis}
                <TabsTourneys 
                    nombreTabla={nombres.misTorneos}
                />         
            </Tab>
            <Tab eventKey="publicTourney" title={nombres.torneosPublicos}>
                {/*<TabIntro />*/}
                {botonAyuda}
                {mostrar && ayudaPublic}
                <TabsTourneys 
                    nombreTabla={nombres.torneosPublicos}
                />
            </Tab>
            <Tab eventKey="finishTourney" title={nombres.finalizados} >
                {/*<TabIntro />*/}
                {botonAyuda}
                {mostrar && ayudaFin}
                <TabsTourneys 
                    nombreTabla={nombres.finalizados}
                />
            </Tab>
        </Tabs>
        </div>
    )
}

export default ComponenteTabs;