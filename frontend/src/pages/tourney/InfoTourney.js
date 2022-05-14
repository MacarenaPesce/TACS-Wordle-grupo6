import React, { Component } from 'react'
import NavbarAut from '../../components/navbar/Navbar';
import Footer from '../../components/footer/Footer';

export default class InfoTourney extends Component{

    render(){
        return (
            <body>
                <header className='NavTourney'>
                    <NavbarAut />
                </header>

                <h1> es un nuevo archivito</h1>
                <button> holaaaaa</button>

                <Footer />
            </body>
        )
    }
}
