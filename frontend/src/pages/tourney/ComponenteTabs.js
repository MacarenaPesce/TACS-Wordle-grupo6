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
            <Tab eventKey="myTourney" title="Mis torneos">
                <TabsTourneys />            
            </Tab>
            <Tab eventKey="publicTourney" title="Publicos">
                
            </Tab>
            <Tab eventKey="finishTourney" title="Finalizados" >
                
            </Tab>
        </Tabs>
    )
}

export default ComponenteTabs;