import React, { useState, useEffect } from 'react'
import { useParams } from 'react-router-dom';
import NavbarAut from '../../components/navbar/Navbar';
import Footer from '../../components/footer/Footer';
import './InfoTourney.css'
import TourneyService from '../../service/TourneyService';
import Not from "../../components/not/Not";
import Handler from "../sesion/Handler";
import AuthService from "../../service/AuthService";

export default function InfoTourney() {
    let { id } = useParams();
    //console.log(id);
    const [tourney, setTourney] = useState({owner: ""});
    //console.log(tourney);
    const [ranking, setRanking] = useState({punctuations: []});
    const [members, setMembers] = useState({members: []});
    const [username, setUsername] = useState('');
    const [puntuacion, setPuntuacion] = useState([]);
    const [puntaje, setPuntaje] = useState(0);

    const [sessionError, setSessionError] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

    const getTourney=() =>{
        TourneyService.getTournamentFromId(id)
            .then(response => {
                setTourney(response.data);
                console.log('Response de torneo obtenida: ')
                console.log(response.data)
            })
            .catch(error => {
                console.log(error)
                Handler.handleSessionErrorFunc(setSessionError, setErrorMessage, error)
            })
          }
     const getRanking =() => {      
        TourneyService.getRanking(id)
            .then(response => {
                setRanking(response.data);
                console.log('Response de ranking obtenida: ')
                console.log(response.data)
            })
            .catch(error => {
                console.log(error)
                Handler.handleSessionErrorFunc(setSessionError, setErrorMessage, error)
            })
          }
     const getMember =() => {
        TourneyService.getMembers(id)
            .then(response => {
              setMembers(response.data);
              console.log('Response de Members obtenida: ')
              console.log(response.data)
            })
            .catch(error => {
                console.log(error)
                Handler.handleSessionErrorFunc(setSessionError, setErrorMessage, error)
            })
          }

    useEffect(() => {
      getTourney();
      getRanking();
      getMember();
      setPuntuacion(ranking.punctuations);
      console.log(tourney);
      console.log("daleeeeeeeeeee")
      validarToken()
    }, []);

    function validarToken() {
        console.log('Ping del token en el store...')
        AuthService.ping()
            .then(response => {
                console.log('Response del ping: ' + response.status)
            })
            .catch(error => {
                console.log(error)
                Handler.handleSessionErrorFunc(setSessionError, setErrorMessage, error)
            })
    }

    let listMembers = (members.members.map((member) =>
      <li className="list-group-item disabled" key={member.username}> {member.username}</li>
    ));

    const filtroUser = (e) => {
      const keyword = e.target.value;
  
      if (keyword !== '') {
        const results = ranking.punctuations.filter((puntuacion) => {
          return puntuacion.user.toLowerCase().startsWith(keyword.toLowerCase());
          // Use the toLowerCase() method to make it case-insensitive
        });
        setPuntuacion(results);
      } else {
        getRanking();
        setPuntuacion(ranking.punctuations);
      }
      setUsername(keyword);
    };  
    
    const filtroPuntaje = (e) => {
      const keyword = e.target.value;
  
      if (keyword !== '') {
        const results = ranking.punctuations.filter((puntuacion) => 
           puntuacion.punctuation == keyword  );
        setPuntuacion(results);
      } else {
        getRanking();
        setPuntuacion(ranking.punctuations);
      }
      setPuntaje(keyword);
    };
    
    const formatDate =(start)=> {
      let fecha = new Date(start)
      let day = fecha.getDate()+1 ;
      let month = (fecha.getMonth() +1)>10?(fecha.getMonth() +1) : '0'+(fecha.getMonth() +1)  ; 
      let year = fecha.getFullYear();
      return day+'/'+month + '/' + year ;
  };    

    return (
      <div>
        <header className='NavTourney'>
            <NavbarAut />
        </header>

          {sessionError &&
              <Not message={errorMessage}/>}

          <div className='body-info'>
            <h1 className='titleInfo'> Torneo N° {id}</h1>
            {/*<button> volver atras</button>*/}

            <container>
              <div className="row">
                <div className="col-5 table-info">
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
                        <td>Inicio: {formatDate(tourney.start)}</td>
                        <td>Fin: {formatDate(tourney.finish)}</td>
                      </tr>
                      <tr>
                        <td>Creador: </td>
                        <td>{tourney.owner.username}</td>
                      </tr>
                      <tr>
                        <td>Puntaje: "puntaje"</td>
                        <td>Puesto: "puesto"</td>
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
                                      aria-label="Search"
                                      value={username}
                                      onChange={filtroUser}
                                      />
                            </form>
                          </div>
                        </td>
                        <td className='searchPunctuation'>
                          <div>
                            <form className="form-inline">
                              <input className="form-control" type="search"
                                      placeholder="Ingrese puntaje"
                                      aria-label="Search"
                                      value={puntaje}
                                      onChange={filtroPuntaje}/>
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
                      {puntuacion && puntuacion.length > 0 ? (
                                puntuacion.map((line) =>(
                                <tr key={line.user}>
                                    <td> {line.position} </td>
                                    <td> {line.user}</td>
                                    <td> {line.punctuation}</td>
                                </tr>
                                ))
                              ):(ranking.punctuations.map((line) =>(
                                <tr key={line.user}>
                                    <td> {line.position} </td>
                                    <td> {line.user}</td>
                                    <td> {line.punctuation}</td>
                                </tr>
                                ))
                              )
                              }
                    </tbody>
                  </table>
                </div>
              </div>
            </container>
          </div>
          <Footer />
      </div>
  );
}


