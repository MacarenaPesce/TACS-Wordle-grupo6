import React from 'react';
import Tabs from 'react-bootstrap/Tabs';
import Tab from 'react-bootstrap/Tab';
import TabsTourneys from './TabsTourneys';
import TabIntro from './TabIntro'
import {BsInfoLg, BsTrashFill} from "react-icons/bs";
import {AiOutlineUserAdd, AiOutlineUsergroupAdd} from "react-icons/ai";

const ComponenteTabs = () => {

    const nombres = {
        misTorneos: 'Mis torneos', 
        torneosPublicos: 'Publicos', 
        finalizados: 'Finalizados',
    }

    //toDO se podria ocultar esta ayuda de funcionamiento con un botón
    const ayudaMis = <div className="alert alert-primary" role="alert">
        <p>
            <p>Te salen tus torneos creados o a los que te agregaste, en todos los que este el userId</p>
            <p>Te salen torneos de otros a los que estas anotado (sin boton para agregar personas)</p>

            <button className="btn btn-warning" type="submit" >
                <AiOutlineUsergroupAdd/> {/* AiOutlineUsergroupAdd -> agregar personas  */}
            </button> Boton para sumar personas (si sos el creador)
            <p></p>
            <button className="btn btn-primary" type="submit" href="/info">
                <BsInfoLg/>
            </button> Información del torneo
            <p></p>
            <button className="btn btn-danger" type="submit">
                <BsTrashFill/>
            </button> Borrar torneo si es propio? salir del torneo si es de otro?

        </p>
    </div>

    const ayudaPublic =     <div className="alert alert-primary" role="alert">
        <p>
            <p>Te salen tus torneos publicos creados (sin boton para agregarte)</p>
            <p>Te salen torneos publicos de otros a los que te podes anotar</p>

            <button className="btn btn-info" type="submit">
                <AiOutlineUserAdd/> {/* AiOutlineUserAdd -> agregarte  */}
            </button> Boton para sumarte (solo si aún no estás participando)
            <p></p>
            <button className="btn btn-primary" type="submit" href="/info">
                <BsInfoLg/>
            </button> Información del torneo
            <p></p>
            <button className="btn btn-danger" type="submit">
                <BsTrashFill/>
            </button> eliminar solo si es tuyo

        </p>
    </div>

    const ayudaFin = <div className="alert alert-primary" role="alert">
        <p>
            <p>Torneos finalizados de los cuales hayas participado/jugado</p>
            <p>Mostrar el ganador en alguna columna</p>
            <button className="btn btn-primary" type="submit" href="/info">
                <BsInfoLg/>
            </button> Boton para ver detalles y ranking final

        </p>
    </div>


    //TODO logica de volver a cargar / request de lista de torneos cada vez que cambias de tab
    return(
        <Tabs defaultActiveKey="myTourney" id="uncontrolled-tab-example" className="mb-3">
            <Tab eventKey="myTourney" title={nombres.misTorneos}>
                {/*<TabIntro />*/}
                {ayudaMis}
                <TabsTourneys 
                    nombreTabla={nombres.misTorneos}
                />         
            </Tab>
            <Tab eventKey="publicTourney" title={nombres.torneosPublicos}>
                {/*<TabIntro />*/}
                {ayudaPublic}
                <TabsTourneys 
                    nombreTabla={nombres.torneosPublicos}
                />
            </Tab>
            <Tab eventKey="finishTourney" title={nombres.finalizados} >
                {/*<TabIntro />*/}
                {ayudaFin}
                <TabsTourneys 
                    nombreTabla={nombres.finalizados}
                />
            </Tab>
            
        </Tabs>
    )
}

export default ComponenteTabs;