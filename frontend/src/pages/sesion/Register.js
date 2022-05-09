import React, { useState, useEffect } from "react";
import './Sesion.css';
import NavbarAut from "../../components/navbar/Navbar";
import Footer from "../../components/footer/Footer";
import { useNavigate } from 'react-router-dom';
import useUser from '../../hooks/useUser';
import {Formulario, ContenedorBotonCentrado, MensajeError, MensajeExito } from "./Formulario";
import { faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Input from '../../components/input/Input'

export default function Register() {
    const [username, setUsername] = useState({campo:'', valido:null});
    const [password, setPassword] = useState({campo:'', valido:null});
    const [password2, setPassword2] = useState({campo:'', valido:null});
    const [email, setEmail] = useState({campo:'', valido:null});
    const [formularioValido, cambiarFormularioValido] = useState(null); 
    const {isLoginLoading, hasLoginError, register, isLogged} = useUser()
    const navigate = useNavigate();

    const expresiones = {
        username: /^[a-zA-Z0-9_-]{3,16}$/, // Letras, numeros, guion y guion_bajo
        password: /^.{3,12}$/, // 3 a 12 digitos.
        email: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/,
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
            password.valido === 'true' &&
            password2.valido === 'true' &&
            email.valido === 'true'
        ){
            cambiarFormularioValido(true);
        } else {
            cambiarFormularioValido(false);
        }
        console.log('Boton register presionado con los datos: ')
        register(username.campo, email.campo, password.campo)
    };

    const validarPassword2 = () => {
        if(password.campo.length > 0){
            if(password.campo !== password2.campo){
                setPassword2((prevState) => {
                    return {...prevState, valido: 'false'}
                });
            } else {
                setPassword2((prevState) => {
                    return {...prevState, valido: 'true'}
                });
            }
        }
    }

    return (
          <div className="login">
              <header className="navGeneral">
                  <NavbarAut />
              </header>
  
              {!isLoginLoading &&
  
              <div className="forms">
                  <h1>Register</h1> 
                      
                    <form onSubmit={handleSubmit}>
                        <Formulario>
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
					        estado={email}
					        cambiarEstado={setEmail}
					        tipo="email"
					        label="Correo Electrónico"
					        placeholder="john@correo.com"
					        name="correo"
					        leyendaError="El correo solo puede contener letras, numeros, puntos, guiones y guion bajo."
					        expresionRegular={expresiones.email}
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
				
                        <Input
					        estado={password2}
					        cambiarEstado={setPassword2}
					        tipo="password"
					        label="Repetir Contraseña"
					        name="password2"
					        leyendaError="Ambas contraseñas deben ser iguales."
					        funcion={validarPassword2}
				        />
                        </Formulario>

                        {/*{formularioValido === false && <MensajeError>
                                <p>
                                    <FontAwesomeIcon icon={faExclamationTriangle}/>
                                    <b>Error:</b> Por favor rellena el formulario correctamente.
                                </p>
                        </MensajeError>}      
                        
                        creo que esto deberia ir en el mensaje de error de abajo*/}

                        <ContenedorBotonCentrado>
                            <button type="submit" className="btn btn-dark btn-lg btn-block">Register</button>
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