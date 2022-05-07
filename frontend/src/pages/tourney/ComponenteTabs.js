import React from 'react';
import Tabs from 'react-bootstrap/Tabs';
import Tab from 'react-bootstrap/Tab';
import { BsTrashFill, BsInfoLg, BsCheckLg } from "react-icons/bs";
import TabsTourneys from './TabsTourneys';

const ComponenteTabs = () => {

    const nombres = {
        misTorneos: 'Mis torneos', 
        torneosPublicos: 'Publicos', 
        finalizados: 'Finalizados',
    }

    return(
        <Tabs defaultActiveKey="myTourney" id="uncontrolled-tab-example" className="mb-3">
            <Tab eventKey="myTourney" title={nombres.misTorneos}>
                <TabsTourneys 
                nombreTabla={nombres.misTorneos}
                />            
            </Tab>
            <Tab eventKey="publicTourney" title={nombres.torneosPublicos}>
                <TabsTourneys />
            </Tab>
            <Tab eventKey="finishTourney" title={nombres.finalizados} >
                <TabsTourneys />
            </Tab>

            {/*<TourneyCreate />*/}
        </Tabs>
    )
}

export default ComponenteTabs;