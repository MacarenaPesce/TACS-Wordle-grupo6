import './Home.css';
import React from 'react';
import NavbarAut from '../components/navbar/Navbar';
import Footer from '../components/footer/Footer';
import AuthService from "../service/AuthService";
import Tourney from "./tourney/Tourney";
import Not from "../components/not/Not";


export default class Home extends React.Component{
  constructor(props){
    super(props);
    this.state={
      logueado: false,
      username: '',
      errorMessage: '',
      sessionError: false
    }
  };

  componentDidMount(){
    if(localStorage.getItem('token')){
      console.log('Ping del token en el store...')
      AuthService.ping()
          .then(response => {
            console.log('Response del ping: '+response.status)
          })
          .catch(error => {
            console.log(error)

            Tourney.handleSessionError(this, error) //Todo mover funcion de tourney, a AuthService? o a donde?
          })
    }
  }

  render(){
    return (  
      <div className='Home' >

        {this.state.sessionError &&
            <Not message={this.state.errorMessage}/>}

        <div className="navGeneral">
          <NavbarAut />
        </div>
      
        <Footer />

      </div>

    );
  }
}


