import React, { Component } from "react";
import './Login.css';
import NavbarAut from "../../components/navbar/Navbar";
import Footer from "../../components/footer/Footer";
import AuthService from "../../service/AuthService";

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
        e.preventDefault() // que hace esta linea?
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
}

