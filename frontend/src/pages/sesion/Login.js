import React, { Component , useState, useEffect, Observer } from "react";

import './Sesion.css';
import NavbarAut from "../../components/navbar/Navbar";
import Footer from "../../components/footer/Footer";
import AuthService from "../../service/AuthService";
import {useLocation} from "wouter";
import useUser from '../../hooks/useUser';


/* Opcion 1: con class (era la inicial) - sin control de usuario */

/*
export default class Login extends Component{

    constructor(){
        super()
        this.state = {
            username: '',
            password: ''
        }
    }

    changeHandler = (e)  => {
        this.setState({[e.target.name]: e.target.value})
    }

    submitHandler = e => {
        e.preventDefault() 
        console.log('Boton login presionado con los datos: ')
        console.log(this.state)
        AuthService.login(this.state.username, this.state.password)
            .then(response => {
                console.log('Response obtenida: ')
                console.log(response)
            })
            .catch(error => {
                console.log(error)
            })
    }

    render(){
        return (

            <body>
                <div className="login">
                    <header>
                        <NavbarAut />
                    </header>                

                    <div className="forms">
                        <form onSubmit={this.submitHandler} className="form-sesion">
                            <h1>Log in</h1>

                            <div className="form-group">
                                <label>Username</label>
                                <input className="form-control" placeholder="Enter username" name="username" onChange={this.changeHandler} />
                            </div>

                            <div className="form-group">
                                <label>Password</label>
                                <input type="password" className="form-control" placeholder="Enter password" name="password" onChange={this.changeHandler} />
                            </div>

                            <div className="form-group">
                                <div className="custom-control custom-checkbox">
                                    <input type="checkbox" className="custom-control-input" id="customCheck1" />
                                    <label className="custom-control-label" htmlFor="customCheck1">Remember me</label>
                                </div>
                            </div>

                            <button type="submit" className="btn btn-dark btn-lg btn-block">Sign in</button>
                            <p className="forgot-password text-right">
                                Forgot <a href="#">password?</a>
                            </p>
                        </form>
                    </div>    
                </div>

                <Footer />
            </body>
        );
    }
}*/





/* Opcion 2:  con clase como la opcion 1, pero con el error de usar useEffect 
        (buscar una alternativa) - con control de usuario */

/*        
const {isLoginLoading, hasLoginError, login, isLogged} = useUser();

const observer = ({ value }) => { 

    useEffect(() => {
        if (isLogged) {
            const [, navigate] = useLocation();
            navigate('/');
            onLogin && onLogin()
        }
        }, [isLogged, navigate, onLogin])
    return null // el componente no representa nada 
  };

class Login extends Component{    

    constructor(){
        super()
        this.state = {
            username: '',
            password: ''
        }
    }

    changeHandler = (e)  => {
        this.setState({[e.target.name]: e.target.value})
    }

    submitHandler = e => {
        e.preventDefault() 
        console.log('Boton login presionado con los datos: ')
        console.log(this.state)
        login( this.state )
        AuthService.login(this.state.username, this.state.password)
            .then(response => {
                console.log('Response obtenida: ')
                console.log(response)
            })
            .catch(error => {
                console.log(error)
            })
    }

    render(){
        return (

            <div>
                <div className="login">
                    <header>
                        <NavbarAut />
                    </header>                

                    <div className="forms">
                        {isLoginLoading && <strong>Checking credentials...</strong>}

                        {!isLoginLoading &&
                            <form onSubmit={this.submitHandler}>
                                <h1>Log in</h1>

                                <div className="form-group">
                                    <label>Username</label>
                                    <input className="form-control" placeholder="Enter username" name="username" onChange={this.changeHandler} />
                                </div>

                                <div className="form-group">
                                    <label>Password</label>
                                    <input type="password" className="form-control" placeholder="Enter password" name="password" onChange={this.changeHandler} />
                                </div>

                                <div className="form-group">
                                    <div className="custom-control custom-checkbox">
                                        <input type="checkbox" className="custom-control-input" id="customCheck1" />
                                        <label className="custom-control-label" htmlFor="customCheck1">Remember me</label>
                                    </div>
                                </div>

                                <Observer value={this.state.username} ><button type="submit" className="btn btn-dark btn-lg btn-block">Sign in</button></Observer>
                                
                                <p className="forgot-password text-right">
                                    Forgot <a href="#">password?</a>
                                </p>
                            </form>
                        }
                        {
                            hasLoginError && <strong>Credentials are invalid</strong>
                        }
                    </div>    
                </div>

                <Footer />
            </div>
        );
    }
}

export default Login;
*/


/*Opcion 3: con funcion que retorna una clase (no funciona) - con control de usuario */

/*
export default function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [, navigate] = useLocation()
    const {isLoginLoading, hasLoginError, login, isLogged} = useUser()
  
    useEffect(() => {
      if (isLogged) {
        navigate('/')
      }
    }, [isLogged, navigate])
  
    const submitHandler = (e) => {
      e.preventDefault();
      login({ username, password })
    };
  
    return class {
        render(){
            return(
                <> 
                <body>
                    <div className="login">
                        <header>
                            <NavbarAut />
                        </header>                

                        <div className="forms">
                            {isLoginLoading && <strong>Checking credentials...</strong>}

                            {!isLoginLoading &&
                                <form onSubmit={this.submitHandler}>
                                    <h1>Log in</h1>

                                    <div className="form-group">
                                        <label>Username</label>
                                        <input className="form-control" placeholder="Enter username" name="username" onChange={this.changeHandler} />
                                    </div>

                                    <div className="form-group">
                                        <label>Password</label>
                                        <input type="password" className="form-control" placeholder="Enter password" name="password" onChange={this.changeHandler} />
                                    </div>

                                    <div className="form-group">
                                        <div className="custom-control custom-checkbox">
                                            <input type="checkbox" className="custom-control-input" id="customCheck1" />
                                            <label className="custom-control-label" htmlFor="customCheck1">Remember me</label>
                                        </div>
                                    </div>

                                    <Observer value={this.state.username} ><button type="submit" className="btn btn-dark btn-lg btn-block">Sign in</button></Observer>
                                    
                                    <p className="forgot-password text-right">
                                        Forgot <a href="#">password?</a>
                                    </p>
                                </form>
                            }
                            {
                                hasLoginError && <strong>Credentials are invalid</strong>
                            }
                        </div>    
                    </div>

                    <Footer />
                </body>
                </>
            );
        }
    }
}
*/

/* Opcion 4: solo con funcion, igual al tutorial - con control de usuario */
/* repo del tutorial: https://github.com/midudev/react-live-coding */

/*
import React, { useState } from "react";
import {useLocation} from "wouter"
import useUser from 'hooks/useUser'
import { useEffect } from "react";
import './Login.css'*/

export default function Login({onLogin}) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [, navigate] = useLocation()
  const {isLoginLoading, hasLoginError, login, isLogged} = useUser()

  useEffect(() => {
    if (isLogged) {
      navigate('/')
      onLogin && onLogin()
    }
  }, [isLogged, navigate, onLogin])

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Boton login presionado con los datos: ')

    login({ username, password })

    AuthService.loginService({username, password})
    .then(response => {
        console.log('Response obtenida: ')
        console.log(response)
    })
    .catch(error => {
        console.log(error)
    })
  };




  return (
    <body>
        <div className="login">
            <header className="navGeneral">
                <NavbarAut />
            </header>
      {isLoginLoading && <strong>Checking credentials...</strong>}
      {!isLoginLoading &&



        <div className="forms">
            <form className="form-sesion" onSubmit={handleSubmit}>
                <h1>Log in</h1>
            
                <div className="form-group">
                    <label>Username</label>
                    <input className="form-control"
                    placeholder="Enter username"
                    onChange={(e) => setUsername(e.target.value)}
                    value={username}
                    />
                </div>

                <div className="form-group">
                    <label>Password</label>
                        <input className="form-control"
                        type="Enter password"
                        placeholder="password"
                        onChange={(e) => setPassword(e.target.value)}
                        value={password}
                        />
                    </div>
                    

                <button type="submit" className="btn btn-dark btn-lg btn-block">Sign in</button>

            </form>
        </div>    
      }
      {
        hasLoginError && <strong>Credentials are invalid</strong>
      }

        </div>

        <Footer />

      </body>
  );
}









    




