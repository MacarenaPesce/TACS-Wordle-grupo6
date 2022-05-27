import React, {useState} from 'react';
import Tabs from 'react-bootstrap/Tabs';
import Tab from 'react-bootstrap/Tab';
import TabsTourneys from './TabsTourneys';
import Ayuda from "./Ayuda";


const ComponenteTabs = () => {

    const nombres = {
        misTorneos: 'Mis torneos', 
        torneosPublicos: 'Publicos', 
        finalizados: 'Finalizados',
    }

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
                {/*<TabIntro />*/}
                {botonAyuda}
                {mostrar && Ayuda.ayudaMis}
                <TabsTourneys 
                    nombreTabla={nombres.misTorneos}
                />         
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