import React, { useState, useEffect } from 'react'
import { Link, useParams } from 'react-router-dom';
import NavbarAut from '../../components/navbar/Navbar';
import Footer from '../../components/footer/Footer';
import './InfoTourney.css'
import TourneyService from '../../service/TourneyService';
import Tourney from "./Tourney";

export default function InfoTourney() {
    debugger
    let { id } = useParams();
    console.log(id);
    let [tourney, setTourney] = useState();
    console.log(tourney);

    let tournament = async () => {
      await TourneyService.getTournamentFromId(id)
      .then(response => {
        setTourney(response.data);
        console.log('Response de torneo obtenida: ')
        console.log(response.data)
      })
      .catch(error => {
          console.log(error)
          Tourney.handleSessionError(this, error)
      })
    }

    useEffect(() => {
      tournament();
      console.log(tourney);
      console.log("daleeeeeeeeeee")
    }, []);


    return (
      <div>
        <header className='NavTourney'>
            <NavbarAut />
        </header>

        <h1 className='titleInfo'> Torneo {id} + nombre:  aaa</h1>
        <button> volver atras</button>

        <container> 
          <div className="row">
            <div className="col-5">
              <table id="customers">
                <tr> 
                  <th></th>
                  <th>Informacion</th>
                </tr>
                <tr>
                  <td>Estado</td>
                  <td></td>
                </tr>
                <tr>
                  <td>Tipo</td>
                  <td>"tipo"</td>
                </tr>
                <tr>
                  <td>lenguaje</td>
                  <td>"lenguaje"</td>
                </tr>
                <tr>
                  <td>Fecha de Inicio - Fin </td>
                  <td>"fecha inicio" - "fecha fin"</td>
                </tr>
                <tr>
                  <td>Creador</td>
                  <td>"creador"</td>
                </tr>
                <tr>
                  <td>Puntaje actual</td>
                  <td>"puntaje"</td>
                </tr>
                <tr>
                  <td>Puesto actual</td>
                  <td>"puesto"</td>
                </tr>
                <tr>
                  <td>Integrantes</td>
                  <td>"int"</td>
                </tr>
              </table>
            </div>
            <div className="col-7">
              ranking
            </div>
          </div>
        </container>
        <Footer />
      </div>
  );
}




