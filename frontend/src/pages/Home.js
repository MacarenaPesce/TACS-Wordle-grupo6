import './Home.css';
import React from 'react';
import NavbarAut from '../components/navbar/Navbar';
import Footer from '../components/footer/Footer';


function Home() {
  return (  
    <div>
      <div className='Home'>
        <body >
          <header>
            <NavbarAut />
          </header>
        </body>
      </div>

      <Footer />
    </div>
  );
}

export default Home;
