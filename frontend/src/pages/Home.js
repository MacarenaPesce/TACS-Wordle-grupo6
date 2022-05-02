import './Home.css';
import React from 'react';
import NavbarAut from '../components/navbar/Navbar';
import Footer from '../components/footer/Footer';


export default class Home extends React.Component{
  constructor(props){
    super(props);
    this.state={
      logueado: false,
      username: ''
    }
  };

  render(){
    return (  
      <div className='Home' >
        <div className="navGeneral">
          <NavbarAut />
        </div>
      
        <Footer />

      </div>

    );
  }
}


