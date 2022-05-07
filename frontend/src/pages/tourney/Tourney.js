import React from 'react';
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


