import React, {useState} from 'react';
import Tabs from 'react-bootstrap/Tabs';
import Tab from 'react-bootstrap/Tab';
import TabsTourneys from './TabsTourneys';
import Ayuda from "./Ayuda";
import TourneyCreate from "./TourneyCreate";
import TourneySubmit from "./TourneySubmit";
import './Panel.css';
import Collapse from "react-bootstrap/Collapse";

const ComponenteTabs = () => {

    const nombres = {
        misTorneos: 'Mis torneos', 
        torneosPublicos: 'Publicos', 
        finalizados: 'Finalizados',
    }
    const [open, setOpen] = useState(true);

    const [mostrar, setMostrar] = useState(false);


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
                <div className="row">
                    <div className="col-md-9">
                        {/*<TabIntro />*/}
                        {botonAyuda}
                        {mostrar && Ayuda.ayudaMis}
                        <TabsTourneys
                            nombreTabla={nombres.misTorneos}
                        />
                            </div>
                    <div className="col-md-3" style={{marginTop: "41px"}}>
                        <button className="btn btn-outline-success my-2 my-sm-0" style={{float: "right"}}
                            onClick={() => setOpen(!open)}
                            aria-controls="collapse-panel"
                            aria-expanded={open}
                        >
                            Panel de control
                        </button>
                        <Collapse in={open} dimension="width" className="Panel">
                            <div id="collapse-panel">
                                <h2 className="flamaDos">{JSON.parse(localStorage.getItem('username'))}</h2>
                                <p></p>
                                <TourneySubmit modal={false}/>
                                <p></p>
                                <TourneyCreate modal={false}/>
                            </div>
                        </Collapse>
                    </div>
                </div>
            </Tab>
            <Tab eventKey="publicTourney" title={nombres.torneosPublicos}>
                {/*<TabIntro />*/}
                {botonAyuda}
                {mostrar && Ayuda.ayudaPublic}
                <TabsTourneys
                    nombreTabla={nombres.torneosPublicos}
                />
            </Tab>
            <Tab eventKey="finishTourney" title={nombres.finalizados} >
                {/*<TabIntro />*/}
                {botonAyuda}
                {mostrar && Ayuda.ayudaFin}
                <TabsTourneys
                    nombreTabla={nombres.finalizados}
                />
            </Tab>
        </Tabs>
        </div>
    )
}

export default ComponenteTabs;