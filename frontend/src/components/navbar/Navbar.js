import Button from 'react-bootstrap/Button'
import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import Container from 'react-bootstrap/Container'
import './Navbar.css';
import { Link } from 'react-router-dom';
import useUser from '../../hooks/useUser';
import {useRoute} from 'wouter'


import IconButton from '@mui/material/IconButton'
import ExitToAppIcon from '@mui/icons-material/ExitToApp';

/* todo: hacer el navbar responsive */

export default function NavbarAut() {
    
    const clickLogin = () =>{
      console.log("Log in")
    };
    
    const clickRegister = () =>{
      console.log("Register")
    };

    const { isLogged, logout} = useUser()
    /*const [match] = useRoute("/login");*/

    const clickLogout = e => {
      console.log("Log out")
      e.preventDefault()
      logout()
    }

    const renderLoginButtons = ({isLogged}) => {
      return isLogged
        ? <div>
            <Navbar className='navBut' bg="dark" variant="dark" >
              <Container>
                <Nav className="me-auto" >
                  <Nav.Link href="/" >Inicio</Nav.Link>
                  <Nav.Link href="/help" >Ayuda</Nav.Link>
                  <Nav.Link href="/diccionary">Diccionario</Nav.Link>
                  <Nav.Link href="/tourney">Torneo</Nav.Link>
                </Nav>
              </Container>
              <Button href="/" onClick={clickLogout} variant="outline-success" className="but-log">Salir</Button>
              <IconButton> <ExitToAppIcon /></IconButton>{ /*para hacerla responsive podemos poner el icono de exit */}
            </Navbar>
          </div>
        : <div>
            <Navbar className='navBut' variant="dark" >
              <Container>
                <Nav className="me-auto" >
                  <Nav.Link href="/" >Inicio</Nav.Link>
                  <Nav.Link href="/help" >Ayuda</Nav.Link>
                  <Nav.Link href="/dictionary">Diccionario</Nav.Link>
                </Nav>
              </Container>
              <Button href="/login" onClick={clickLogin} className="btn-dark but-log" variant="outline-success">Ingresar</Button>
              {/*<Button href="/login" onClick={clickLogin} variant="outline-success" className="but-log">Ingresar</Button>*/}
              <Button href="/register" onClick={clickRegister} variant="outline-success" className="but-log">Registrarse</Button>
            </Navbar>
          </div>
    }
  /*
  const content = match
    ? null
    : renderLoginButtons({isLogged})*/
  
  return (
    <header>
      {renderLoginButtons({isLogged})}
    </header>
  )
}  

