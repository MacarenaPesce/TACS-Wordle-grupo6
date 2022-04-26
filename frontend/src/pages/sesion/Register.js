import React, { Component } from "react";
import './Sesion.css';
import NavbarAut from "../../components/navbar/Navbar";
import Footer from "../../components/footer/Footer";
import AuthService from "../../service/AuthService";

export default class Register extends Component{

    constructor(){
        super()
        this.state = {
            username: '',
            email: '',
            password: ''
        }
    }

    changeHandler = (e)  => {
        this.setState({[e.target.name]: e.target.value})
    }

    submitHandler = e => {
        e.preventDefault()
        console.log('Boton register presionado con los datos: ')
        console.log(this.state)
        AuthService.register(this.state.username, this.state.email, this.state.password)
            .then(response => {
                console.log('Response obtenida: ')
                console.log(response.data)
            })
            .catch(error => {
                console.log(error)
            })
    }

    render(){
        return (

            <body>
                <div className="login"> {/* todo: rename classname and *.css */}
                    <header>
                        <NavbarAut />
                    </header>                

                    <div className="forms">
                        <form onSubmit={this.submitHandler} className="form-sesion">
                            <h1>Register</h1>

                            <div className="form-group">
                                <label>Username</label>
                                <input className="form-control" placeholder="Enter username" name="username" onChange={this.changeHandler} />
                            </div>

                            <div className="form-group">
                                <label>Email</label>
                                <input type="email" className="form-control" placeholder="Enter email" name="email" onChange={this.changeHandler} />
                            </div>

                            <div className="form-group">
                                <label>Password</label>
                                <input type="password" className="form-control" placeholder="Enter password" name="password" onChange={this.changeHandler} />
                            </div>
                            {/*}
                            <div className="form-group">
                                <label>Repeat password</label>
                                <input type="password" className="form-control" placeholder="Repeat password" />
                            </div>
                            */}

                            <div className="form-group">
                                <div className="custom-control custom-checkbox">
                                    <input type="checkbox" className="custom-control-input" id="customCheck1" />
                                    <label className="custom-control-label" htmlFor="customCheck1">Remember me</label>
                                </div>
                            </div>

                            <button type="submit" className="btn btn-dark btn-lg btn-block">Register</button>
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