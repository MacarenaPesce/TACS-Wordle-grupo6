import './Home.css';
import React from 'react';
import NavbarAut from '../components/navbar/Navbar';
import Footer from '../components/footer/Footer';


function Home() {
  return (  
    <body className='Home' >
      <header>
        <NavbarAut />
      </header>
    
      <Footer />

    </body>

  );
}

export default Home;
