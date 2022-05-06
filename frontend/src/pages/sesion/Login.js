import React, { useState, useEffect } from "react";
import './Sesion.css';
import NavbarAut from "../../components/navbar/Navbar";
import Footer from "../../components/footer/Footer";
import { useNavigate } from 'react-router-dom';
import useUser from '../../hooks/useUser';

export default function Login({onLogin}) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const {isLoginLoading, hasLoginError, login, isLogged} = useUser()
  const navigate = useNavigate();

  useEffect(() => {
    if (isLogged) {
        navigate('/');
       // onLogin && onLogin()
    }
  }, [isLogged, navigate, onLogin])

  const handleSubmit = (e) => {
    e.preventDefault();
    login(username, password)
  };

  return (
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
                            type="password"
                            placeholder="Enter password"
                            onChange={(e) => setPassword(e.target.value)}
                            value={password}
                            />
                    </div>      

                    <button type="submit" className="btn btn-dark btn-lg btn-block">Sign in</button>

                </form>
            </div>    
            }
            {   hasLoginError && <strong>Credentials are invalid{/*todo: tiene que ser un modal */}</strong> }

        <Footer />
    </div>
  );
}









    




