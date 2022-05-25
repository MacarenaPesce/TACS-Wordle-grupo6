import React, { useState, useEffect } from "react";
import './Sesion.css';
import NavbarAut from "../../components/navbar/Navbar";
import Footer from "../../components/footer/Footer";
import { useNavigate } from 'react-router-dom';
import useUser from '../../hooks/useUser';
import Input from '../../components/input/Input'
import {ContenedorBotonCentrado, MensajeError, MensajeExito } from "./Formulario";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';

export default function Login({onLogin}) {
  const [username, setUsername] = useState({campo:'', valido:null});
  const [password, setPassword] = useState({campo:'', valido:null});
  const [formularioValido, cambiarFormularioValido] = useState(null); 
  const {isLoginLoading, hasLoginError, login, isLogged} = useUser()
  const navigate = useNavigate();

  const expresiones = {
    username: /^[a-zA-Z0-9_-]{3,16}$/, // Letras, numeros, guion y guion_bajo
    password: /^.{3,12}$/, // 3 a 12 digitos.
}

  useEffect(() => {
    if (isLogged) {
        navigate('/');
    }
  }, [isLogged, navigate])

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if(
      username.valido === 'true' &&
      password.valido === 'true'
    ){
      cambiarFormularioValido(true);
    } else {
      cambiarFormularioValido(false);
    }
    console.log('Boton loggin presionado con los datos: ')

    login(username, password)
  };

  let spinner = (<div className="spinner-border text-light" role="status">
                    <span className="visually-hidden">Loading...</span>
                </div>);

  return (
      <div className="login">
            <header className="navGeneral">
                <NavbarAut />
            </header>
            
            {isLoginLoading && <div>{spinner}<strong>Checking credentials...{/*todo: tiene que ser un modal */}</strong>{spinner}</div>}
            {!isLoginLoading &&

            <div className="forms">
                <form className="form-sesion" onSubmit={handleSubmit}>
                    <h1>Log in</h1>

                    <Input
                            estado={username}
                            cambiarEstado={setUsername}
                            tipo="text"
                            label="Usuario"
                            /*placeholder="Ingrese su nombre de usuario"*/
                            placeholder="john123"
                            name="nombre"
                            leyendaError="El usuario tiene que ser de 3 a 16 dígitos y solo puede contener numeros, letras y guion bajo."
                            expresionRegular={expresiones.username}
                    />

                    <Input
                            estado={password}
                            cambiarEstado={setPassword}
                            tipo="password"
                            label="Contraseña"
                            name="password1"
                            leyendaError="La contraseña tiene que ser de 3 a 12 dígitos."
                            expresionRegular={expresiones.password}
				            />   

                    <ContenedorBotonCentrado>
                            <button type="submit" className="btn btn-dark btn-lg btn-block">Sign in</button>
                            {formularioValido === true && <MensajeExito>Formulario enviado exitosamente!</MensajeExito>}
                    </ContenedorBotonCentrado>

                </form>
            </div>    
            }
            {   hasLoginError &&  <MensajeError>
                    <p>
                        <FontAwesomeIcon icon={faExclamationTriangle}/>
                        <b>Error:</b> Por favor rellena el formulario correctamente.                        
                    </p>
                </MensajeError> }

        <Footer />
    </div>
  );
}









    




