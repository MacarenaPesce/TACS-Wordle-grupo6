import React, { Component , useState, useEffect, Observer } from "react";

import './Sesion.css';
import NavbarAut from "../../components/navbar/Navbar";
import Footer from "../../components/footer/Footer";
import AuthService from "../../service/AuthService";
import {useLocation} from "wouter";
import useUser from '../../hooks/useUser';



export default function Login({onLogin}) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [, navigate] = useLocation()
  const {isLoginLoading, hasLoginError, login, isLogged} = useUser()

  useEffect(() => {
    if (isLogged) {
      navigate('/');
      onLogin && onLogin()
    }
  }, [isLogged, navigate, onLogin])

  const handleSubmit = (e) => {
    e.preventDefault();
    login(username, password)
  };




  return (
    <body>
        <div className="login">
            <header className="navGeneral">
                <NavbarAut />
            </header>
      {isLoginLoading && <strong>Checking credentials...{/*todo: tiene que ser un modal */}</strong>}
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
        hasLoginError && <strong>Credentials are invalid{/*todo: tiene que ser un modal */}</strong> 
      }

        </div>

        <Footer />
      </body>
  );
}









    




