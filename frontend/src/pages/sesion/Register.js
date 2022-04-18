import React, { Component } from "react";
import './Login.css';
import NavbarAut from "../../components/navbar/Navbar";
import Footer from "../../components/footer/Footer";

export default class Register extends Component{
    render(){
        return (

            <body>
                <div className="login"> {/* todo: rename classname and *.css */}
                    <header>
                        <NavbarAut />
                    </header>                

                    <div className="forms">
                        <form>
                            <h1>Register</h1>

                            <div className="form-group">
                                <label>Username</label>
                                <input type="email" className="form-control" placeholder="Enter username" />
                            </div>

                            <div className="form-group">
                                <label>Email</label>
                                <input type="email" className="form-control" placeholder="Enter email" />
                            </div>

                            <div className="form-group">
                                <label>Password</label>
                                <input type="password" className="form-control" placeholder="Enter password" />
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