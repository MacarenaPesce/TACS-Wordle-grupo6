import React, { useState, useEffect } from "react";
import './Sesion.css';
import NavbarAut from "../../components/navbar/Navbar";
import Footer from "../../components/footer/Footer";
import { useNavigate } from 'react-router-dom';
import useUser from '../../hooks/useUser';
import Input from '../../components/input/Input'
import {ContenedorBotonCentrado } from "./Formulario";
import NotificationBar from "../../components/notificationBar/NotificationBar";
import {toast} from 'react-hot-toast';


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
        // onLogin && onLogin()
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
    //console.log('Boton loggin presionado con los datos: ')

    login(username.campo, password.campo)
  };

  function chequeo(){
    if(formularioValido === true)
      toast.success("Formulario enviado exitosamente!");

    if (hasLoginError)
      toast.error("Error: Por favor rellena el formulario correctamente. Fallaron tus credenciales.")                                   
    }

  let spinner = (<div className="spinner-border text-light" role="status">
                    <span className="visually-hidden">Loading...</span>
                </div>);

  return (
      <div className="login">
            <header className="navGeneral">
                <NavbarAut />
            </header>
            
            {isLoginLoading && <div>{spinner}<strong>Chequeando credenciales... Aguarde un momento...</strong>{spinner}</div>/*todo: esto tendria que ir con un toast.loading('... ') */} 
            
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
                            name="password"
                            leyendaError="La contraseña tiene que ser de 3 a 12 dígitos."
                            expresionRegular={expresiones.password}
				            />   

                    <ContenedorBotonCentrado>
                            <button type="submit" className="btn btn-dark btn-lg btn-block" onClick={chequeo()}>Sign in</button>
                            
                    </ContenedorBotonCentrado>
                    
                </form>

                <NotificationBar />
            </div>    
            }

        <Footer />
    </div>
  );
}









    




