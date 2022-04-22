import Button from 'react-bootstrap/Button'
import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import Container from 'react-bootstrap/Container'
import './Navbar.css';
import { Link } from 'react-router-dom';


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
        {/*todo: hacer el navbar responsive y editar los botones para ver cuando se tienen que mostrar y cuando no*/}
        <Navbar className='navBut' bg="dark" variant="dark" >
          <Container>
            <Nav className="me-auto" >
              <Nav.Link href="/help" >Ayuda</Nav.Link>
              <Nav.Link href="#diccionary">Diccionario</Nav.Link>
              <Nav.Link href="#tourney">Torneo</Nav.Link>
            </Nav>
          </Container>
  
          <Link to='/login'><Button onClick={login} variant="outline-success" className="but-log">Iniciar Sesión</Button></Link>
          <Button onClick={logout} variant="outline-success" className="but-log">Cerrar Sesión</Button>
          <Link to='/register'><Button onClick={register} variant="outline-success" className="but-log">Registrarse</Button></Link>
        </Navbar>
        
        {/*
        <Navbar bg="light" variant="light">
          <Container>
          <Nav className="me-auto">
            <Nav.Link href="#home">Ayuda</Nav.Link>
            <Nav.Link href="#features">Diccionario</Nav.Link>
            <Nav.Link href="#pricing">Torneo</Nav.Link>
          </Nav>
          </Container>
  
          <Button onClick={login} variant="outline-success">Log in</Button>
          <Button onClick={logout} variant="outline-success">Log out</Button>
          <Button onClick={register} variant="outline-success">Register</Button>
        </Navbar>
    */}
  
      </div>
    );
  }
  
export default NavbarAut;