import React, { Component , useState, useEffect, Observer } from "react";

import './Sesion.css';
import NavbarAut from "../../components/navbar/Navbar";
import Footer from "../../components/footer/Footer";
import AuthService from "../../service/AuthService";
import {useLocation} from "wouter"
import useUser from '../../hooks/useUser'

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
    } */

/*
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

export default Login;*/

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
