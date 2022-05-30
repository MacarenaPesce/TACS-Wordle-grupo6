import React, { useState, useEffect } from 'react'
import { useParams } from 'react-router-dom';
import NavbarAut from '../../components/navbar/Navbar';
import Footer from '../../components/footer/Footer';
import './InfoTourney.css'
import TourneyService from '../../service/TourneyService';
import Tourney from "./Tourney";

export default function InfoTourney() {
    //debugger
    let { id } = useParams();
    console.log(id);
    const [tourney, setTourney] = useState({owner: ""});
    console.log(tourney);
    const [ranking, setRanking] = useState({punctuations: []});
    const [members, setMembers] = useState({members: []});

    function tournament(){
        TourneyService.getTournamentFromId(id)
            .then(response => {
                setTourney(response.data);
                console.log('Response de torneo obtenida: ')
                console.log(response.data)
            })
            .catch(error => {
                console.log(error)
                //Tourney.handleSessionError(this, error) //todo esto no hace nada sin usar las variables de estado sessionError y errorMessage, solo funciona en class
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
        TourneyService.getMembers(id)
            .then(response => {
              setMembers(response.data);
              console.log('Response de Members obtenida: ')
              console.log(response.data)
            })
            .catch(error => {
                console.log(error)
            })

    }

    useEffect(() => {
      //debugger
      tournament();
      console.log(tourney);
      console.log("daleeeeeeeeeee")
    }, []);

    let fullRanking = (ranking.punctuations.map((line) =>
        <tr key={line.user}>
            <td> ?? </td>
            <td> {line.user}</td>
            <td> {line.punctuation}</td>
        </tr>));
    
    let listMembers = (members.members.map((member) =>
      <li className="list-group-item disabled" key={member.username}> {member.username}</li>
    ));

    return (
      <div>
        <header className='NavTourney'>
            <NavbarAut />
        </header>

        <h1 className='titleInfo'> Torneo N° {id}</h1>
        {/*<button> volver atras</button>*/}

        <container> 
          <div className="row">
            <div className="col-5">
              <table id="customers">
                <thead>
                  <tr> 
                    <th colSpan={2}  className='thTitulo'>Informacion</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>Nombre:  </td>
                    <td>{tourney.name}</td>
                  </tr>
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
                          {listMembers}
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
                    <th colSpan={3} className='thTitulo'>Ranking</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td colSpan={2} className="searchUsername">
                      <div>
                        <form className="form-inline">
                          <input className="form-control " type="search" 
                                  placeholder="Ingrese nombre del usuario"
                                  aria-label="Search"/>
                                  {/*value={this.name}
                                  onChange={this.filtro}*/}
                        </form>
                      </div>
                    </td>
                    <td className='searchPunctuation'> 
                      <div>
                        <form className="form-inline">
                          <input className="form-control" type="search" 
                                  placeholder="Ingrese puntaje"
                                  aria-label="Search"/>
                                  {/*value={this.name}
                                  onChange={this.filtro}*/}
                        </form>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <th className='encabezado'> 
                      Puesto
                    </th>
                    <th className='encabezado'> 
                      Miembro
                    </th>
                    <th className='encabezado'> 
                      Puntaje
                    </th>
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


