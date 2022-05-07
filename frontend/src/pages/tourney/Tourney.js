import React, { Component } from 'react';
import NavbarAut from '../../components/navbar/Navbar';
import Footer from '../../components/footer/Footer';
import './Tourney.css'
import ComponenteTabs from './ComponenteTabs';


export default function Tourney(){
    return(
        <div className='tourney'> 
            <header className='NavTourney'>
                <NavbarAut />
            </header>
        
            <ComponenteTabs />

            <Footer />
        </div>
    );
}

/*export default class Tourney extends Component(){
    
    constructor(){
        super()
        this.state = {
            myTourneys: []
        }
    }


    
    return(
        <div className='tourney'> 
            <header className='NavTourney'>
                <NavbarAut />
            </header>
        
            <ComponenteTabs />

            <Footer />
        </div>
    );
}
*/


