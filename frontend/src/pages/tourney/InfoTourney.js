import React, { useState, useEffect } from 'react'
import { Link, useParams } from 'react-router-dom';
import NavbarAut from '../../components/navbar/Navbar';
import Footer from '../../components/footer/Footer';
import './InfoTourney.css'
import TourneyService from '../../service/TourneyService';
import Tourney from "./Tourney";
import BotonesTorneos from "./BotonesTorneos";

export default function InfoTourney() {
    //debugger
    let { id } = useParams();
    console.log(id);
    const [tourney, setTourney] = useState({owner: ""});
    console.log(tourney);
    const [ranking, setRanking] = useState({punctuations: []});

    let PUESTOS = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]

    function tournament(){
        TourneyService.getTournamentFromId(id)
            .then(response => {
                setTourney(response.data);
                console.log('Response de torneo obtenida: ')
                console.log(response.data)
            })
            .catch(error => {
                console.log(error)
                Tourney.handleSessionError(this, error) //todo esto no hace nada sin usar las variables de estado sessionError y errorMessage, solo funciona en class
            })
        TourneyService.getRanking(id)
            .then(response => {
                setRanking(response.data);
                console.log('Response de ranking obtenida: ')
                console.log(response.data)
            })
            .catch(error => {
                console.log(error)
            })
    }

    useEffect(() => {
      tournament();
      console.log(tourney);
      console.log("daleeeeeeeeeee")
    }, []);

    let listRanking = PUESTOS.map((puesto) =>
    <li className="list-group-item disabled"> {puesto}</li>
    );
    let fullRanking = (ranking.punctuations.map((line) =>
        <tr key={line.user}>
            <td> ? </td>
            <td> {line.user}</td>
            <td> {line.punctuation}</td>
        </tr>));

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
                    <td>{tourney.state}</td>
                  </tr>
                  <tr>
                    <td>Tipo: </td>
                    <td>{tourney.type}</td>
                  </tr>
                  <tr>
                    <td>Lenguaje: </td>
                    <td>{tourney.language}</td>
                  </tr>
                  <tr>
                    <td colSpan={2}>Inicio: {tourney.start} - Fin: {tourney.finish} </td>
                  </tr>
                  <tr>
                    <td>Creador: </td>
                    <td>{tourney.owner.username}</td>
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
            <div className="col-7 table-ranking">
              <table id="customers">
                <thead>
                  <tr> 
                    <th colSpan={3}>Ranking</th>
                  </tr>
                </thead>
                <tbody>
                  <tr className='encabezado'>
                    <td> 
                      Puesto
                    </td>
                    <td> 
                      Miembro
                    </td>
                    <td> 
                      Puntaje
                    </td>
                  </tr>
                  {fullRanking}
                </tbody>
              </table>              
            </div>
          </div>
        </container>
        <Footer />
      </div>
  );
}


