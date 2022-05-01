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

/* todo: casos del navbar

  si NO esta logueado:
    - login y register
    - si toca torneo lo direcciona a login
    - acceso a dicc y help
  si esta logueado:
    - cerrar sesion
    - acceso a todo

  cuando hace el login: seteo true a esta logueado o cuando hace el register

  todo: hacer el navbar responsive y editar los botones para ver cuando se tienen que mostrar y cuando no

*/

/*Opcion 1: sin control de logueo  */
/*
function NavbarAut() {
  const login = () =>{
    console.log("Log in")
  };

  const logout = () =>{
    console.log("Log out")
  };

  const register = () =>{
    console.log("Register")
  };
  
  return ( 
    
    <div>
      <Navbar className='navBut' bg="dark" variant="dark" clas >
        <Container>
        <Nav className="me-auto" >
          <Nav.Link href="/" >Inicio</Nav.Link>
          <Nav.Link href="/help" >Ayuda</Nav.Link>
          <Nav.Link href="/dictionary">Diccionario</Nav.Link>
          <Nav.Link href="#tourney">Torneo</Nav.Link>
        </Nav>
        </Container>

        <Link to='/login'><Button onClick={login} variant="outline-success" className="but-log">Ingresar</Button></Link>
        <Link to='/register'><Button onClick={register} variant="outline-success" className="but-log">Registrarse</Button></Link>
        <Button onClick={logout} variant="outline-success" className="but-log">Salir</Button>
      </Navbar>

    </div>
  );
}

export default NavbarAut;
*/

/*Opcion 2: con control de logueo  */

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
            <Navbar className='navBut' bg="dark" variant="dark" >
              <Container>
                <Nav className="me-auto" >
                  <Nav.Link href="/" >Inicio</Nav.Link>
                  <Nav.Link href="/help" >Ayuda</Nav.Link>
                  <Nav.Link href="/diccionary">Diccionario</Nav.Link>
                </Nav>
              </Container>
              <Button href="/login" onClick={clickLogin} variant="outline-success" className="but-log">Ingresar</Button>
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

