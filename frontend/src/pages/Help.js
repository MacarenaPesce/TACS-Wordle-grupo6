import React, { Component } from "react";
import './sesion/Login.css';
import NavbarAut from "../components/navbar/Navbar";
import Footer from "../components/footer/Footer";
import HelpService from "../service/HelpService";
import './Help.css';

export default class Help extends Component{

    constructor(){
        super()
        this.state = {
            language: 'ES',
            yellow: '',
            grey: '',
            solution: '',
            wordsResponse: []
        }
    }

    changeHandler = (e)  => {
        this.setState({[e.target.name]: e.target.value})
    }

    submitHandler = e => {
        e.preventDefault()
        console.log('Boton ayuda presionado con los datos: ')
        let body = {
            yellow: this.state.yellow,
            grey: this.state.grey,
            solution: this.state.solution
        }
        console.log(body, this.state.language)
        HelpService.postHelp(body, this.state.language)
            .then(response => {
                console.log('Response obtenida: ')
                console.log(response.data)
                this.setState({wordsResponse: response.data.possibleWords})
            })
            .catch(error => {
                console.log(error)
            })
    }

    render(){
        let listWords = this.state.wordsResponse.map((word) =>
            <li key={word}>{word}</li>
        );

        return (

            <body className="help">
                    <header>
                        <NavbarAut />
                    </header>

                    <div className="instrucciones">
                        {/*<h1>WordleCheats.exe</h1>*/}
                        {/*todo: alinear verticalmente img, lo puse con margin pero no queda centrado*/}
                        <img src={require('../img/comojugar.png')} alt="Como jugar" />
                        <p></p>
                    </div>

                    <div className="forms"> 
                        <div className="resultados">
                        
                            <form onSubmit={this.submitHandler}>
                                
                                <div className="opciones">
                                    <div className="form-group">
                                        <div><label><h5>Idioma</h5></label></div>
                                        <select name="language" onChange={this.changeHandler}>
                                            <option value="ES">Español</option>
                                            <option value="EN">English</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="opciones">
                                    <div className="form-group">
                                        <label><h5>Amarillas</h5></label>
                                        <input type="text" pattern="[A-Za-z]*" title="Solo letras" className="form-control" placeholder="AO" name="yellow" onChange={this.changeHandler} />
                                    </div>
                                </div>
                                
                                <div className="opciones">
                                    <div className="form-group">
                                        <label><h5>Grises</h5></label>
                                        <input type="text" pattern="[A-Za-z]*" title="Solo letras" className="form-control" placeholder="HRS" name="grey" onChange={this.changeHandler} />
                                    </div>
                                </div>

                                <div className="opciones">
                                    <div className="form-group">
                                        <label><h5>Solucion actual incompleta</h5></label>
                                        <input type="text" pattern="[A-Za-z_]{5}" title="5 letras o _" className="form-control" placeholder="_L_W_" name="solution" onChange={this.changeHandler} />
                                    </div>
                                </div>

                                <p></p>
                                <button type="submit" className="btn btn-primary"><h5>Ayuda 🥺👉👈</h5></button>
                                <p></p>

                                {/*todo: cambiar como se muestran las posibles soluciones, podria ser a la derecha o abajo con un selector*/}
                                <h5><label><h5>Soluciones posibles</h5></label></h5>
                                <div className="bg1">
                                    {listWords}
                                </div>
                            </form>
                        </div>
                    </div>
                    
                
                    <Footer />

            </body>
        );
    }
}