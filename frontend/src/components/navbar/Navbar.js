import Button from 'react-bootstrap/Button'
import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import Container from 'react-bootstrap/Container'
import './Navbar.css';
import useUser from '../../hooks/useUser';

export default function NavbarAut() {
   
    const clickLogin = () =>{
      console.log("Log in")
    };
    
    const clickRegister = () =>{
      console.log("Register")
    };

    const { isLogged, logout} = useUser()
    /*const [match] = useRoute("/login");*/

    /*useEffect(() => {
        console.log("USE EFFECT !!!!!!!!!!!! para validar sesion, deberia estar aca el ping de sesion
                                        en vez de solo home, pero que no lo tome /login ni /register")
        },[])*/

    const clickLogout = e => {
      console.log("Log out")
      e.preventDefault()
      logout()
    }

    const renderLoginButtons = ({isLogged}) => {
      return isLogged
        ? <div>
            <Navbar className='navBut'  variant="dark" >
              <Container>
                <Nav className="me-auto" >
                  <Nav.Link href="/" >Inicio{/*<img src = "Wlogo.svg" alt="W logo" className="logo"/>*/}</Nav.Link>
                  <Nav.Link href="/help" >Ayuda</Nav.Link>
                  <Nav.Link href="/dictionary">Diccionario</Nav.Link>
                  <Nav.Link href="/tourney">Torneo</Nav.Link>
                </Nav>
              </Container>
              <Button href="/" onClick={clickLogout} variant="outline-success" className="btn-dark but-log">Salir</Button>
              {/*<IconButton> <ExitToAppIcon /></IconButton>{ /*para hacerla responsive podemos poner el icono de exit */}
            </Navbar>
          </div>
        : <div>
            <Navbar className='navBut'  variant="dark" >
              <Container>
                <Nav className="me-auto" >
                  <Nav.Link href="/" >Inicio{/*<img src = "Wlogo.svg" alt="W logo" className="logo"/>*/}</Nav.Link>
                  <Nav.Link href="/help" >Ayuda</Nav.Link>
                  <Nav.Link href="/dictionary">Diccionario</Nav.Link>
                </Nav>
              </Container>
              <Button href="/login" onClick={clickLogin} className="btn-dark but-log" variant="outline-success">Ingresar</Button>
              <Button href="/register" onClick={clickRegister} className="btn-dark but-log" variant="outline-success">Registrarse</Button>
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

