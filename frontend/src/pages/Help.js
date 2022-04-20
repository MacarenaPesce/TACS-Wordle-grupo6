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

            <body>
            <div className="login">
                <header>
                    <NavbarAut />
                </header>

                <div className="forms">
                    <form onSubmit={this.submitHandler}>
                        <h1>WordleCheats.exe</h1>
                        <img src={require('../img/comojugar.png')} alt="Como jugar" width="500" height="600" />
                        <p></p>

                        <div className="form-group">
                            <div><label>Idioma</label></div>
                            <select name="language" onChange={this.changeHandler}>
                                <option value="ES">Español</option>
                                <option value="EN">English</option>
                            </select>
                        </div>

                        <div className="form-group">
                            <label>Amarillas</label>
                            <input type="text" pattern="[A-Za-z]*" title="Solo letras" className="form-control" placeholder="AO" name="yellow" onChange={this.changeHandler} />
                        </div>

                        <div className="form-group">
                            <label>Grises</label>
                            <input type="text" pattern="[A-Za-z]*" title="Solo letras" className="form-control" placeholder="HRS" name="grey" onChange={this.changeHandler} />
                        </div>

                        <div className="form-group">
                            <label>Solucion actual incompleta</label>
                            <input type="text" pattern="[A-Za-z_]{5}" title="5 letras o _" className="form-control" placeholder="_L_W_" name="solution" onChange={this.changeHandler} />
                        </div>

                        <p></p>
                        <button type="submit" className="btn btn-dark btn-lg btn-block">Ayuda 🥺👉👈</button>
                        <p></p>


                        <h5><label>Soluciones posibles</label></h5>
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