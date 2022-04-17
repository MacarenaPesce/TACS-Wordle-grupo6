import Button from 'react-bootstrap/Button'
import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import Container from 'react-bootstrap/Container'
import './Navbar.css';


function Header() {
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
            <Nav.Link href="#home" >Ayuda</Nav.Link>
            <Nav.Link href="#features">Diccionario</Nav.Link>
            <Nav.Link href="#pricing">Torneo</Nav.Link>
          </Nav>
          </Container>
  
          <Button onClick={login} variant="outline-success" className="but-log" >Iniciar Sesión</Button>
          <Button onClick={logout} variant="outline-success" className="but-log">Cerrar Sesión</Button>
          <Button onClick={register} variant="outline-success" className="but-log">Registrarse</Button>
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
  
export default Header;