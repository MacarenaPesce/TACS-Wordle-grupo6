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
        {/*<button> volver atras</button>*/}

        <container> 
          <div className="row">
            <div className="col-5">
              <table id="customers">
                <thead>
                  <tr> 
                    <th colSpan={2}>Informacion</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>Estado:  </td>
                    <td>"state"</td>
                  </tr>
                  <tr>
                    <td>Tipo: </td>
                    <td>"tipo"</td>
                  </tr>
                  <tr>
                    <td>Lenguaje: </td>
                    <td>"lenguage"</td>
                  </tr>
                  <tr>
                    <td colSpan={2}>Inicio: "fecha inicio" - Fin: "fecha fin" </td>
                  </tr>
                  <tr>
                    <td>Creador: </td>
                    <td>"creador"</td>
                  </tr>
                  <tr>
                    <td  colSpan={2}>Puntaje: "puntaje" - Puesto: "puesto"</td>
                  </tr>
                  <tr>
                    <td>Integrantes: </td>
                    <td>
                      <ul className="list-group scrollbar-success">
                          <li>hola</li>
                          <li>chau</li> 
                          <li>si</li>  
                          <li>no</li>
                      </ul>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div className="col-7">
              
            </div>
          </div>
        </container>
        <Footer />
      </div>
  );
}




