import React, { Component } from 'react'
import { Link, useParams } from 'react-router-dom';
import NavbarAut from '../../components/navbar/Navbar';
import Footer from '../../components/footer/Footer';
import './InfoTourney.css'

export default function InfoTourney() {
    const { id } = useParams();

    return (
      <div>
        <header className='NavTourney'>
            <NavbarAut />
        </header>

        <h1 className='titleInfo'> Id del torneo {id}</h1>
        <button> Boton para volver atras</button>
        <p> Aca iria:
            Nombre y id,
            Estado del torneo,
            tipo, lenguaje,
            Fechas del torneo,
            Creador,
            Integrantes,
            Tu puntaje y tu puesto actual,
            Ranking a la derecha
        </p>

        <Footer />
      </div>
    );
  }