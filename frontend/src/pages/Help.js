import React, { Component } from "react";
import NavbarAut from "../components/navbar/Navbar";
import Footer from "../components/footer/Footer";
import HelpService from "../service/HelpService";
import './Help.css';
import { ListGroup } from "react-bootstrap";

export default class Help extends Component{

    constructor(){
        super()
        this.state = {
            language: 'ES',
            yellow: '',
            grey: '',
            solution: '',
            wordsResponse: [],
            visibility: "none",
            loading: false,
            error: false
        }
    }

    changeHandler = (e)  => {
        this.setState({[e.target.name]: e.target.value})
    }

    submitHandler = e => {
        e.preventDefault()
        this.setState({loading: true, error: false})
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
                this.setState({wordsResponse: response.data.possibleWords, loading: false})
            })
            .catch(error => {
                this.setState({loading: false, error: true})
                console.log(error)
            })
    }

    render(){
        let listWords = this.state.wordsResponse.map((word) =>
            <li className="list-group-item disabled" key={word}> {word}</li>
        );
        let redSpinner = (<div className="spinner-border text-danger" role="status">
                            <span className="visually-hidden">Loading...</span>
                        </div>);
        let greenSpinner = (<div className="spinner-border text-success" role="status">
            <span className="visually-hidden">Loading...</span>
        </div>);
        let loadButton = (<button className="btn btn-primary" type="button" disabled>
                            <span className="spinner-grow spinner-grow-sm" role="status" aria-hidden="true"></span>
                            Loading...
                        </button>);

        let result;
        let display;
        if (listWords.length > 0) {
            result =    <ul className="list-group scrollbar-sunny-morning">
                            {listWords}
                        </ul>;
        } else {
            result =    <div className="alert alert-danger" role="alert">
                            ⚠️No existen coincidencias⚠️
                         </div>;
        }

        if (this.state.loading) {
            display =   <div className="alert alert-secondary" role="alert">
                            {greenSpinner}
                            {redSpinner}
                            {loadButton}
                            {redSpinner}
                            {greenSpinner}
                        </div>;
        } else {
            display = result;
        }

        if(this.state.error){
            display = <div className="alert alert-danger" role="alert">
                        ⚠️Error encontrado, contacte al programador⚠️
                    </div>;
        }

        return (

            <div className="help">
                    <header className="navGeneral">
                        <NavbarAut />
                    </header>

                    <div className="container">
                        <div className="row">
                            <div className="col">                              
                                <div className="instrucciones">
                                    {/*<h1>WordleCheats.exe</h1>*/}
                                    {/*todo: alinear verticalmente img, lo puse con margin pero no queda centrado*/}
                                    <img src={require('../img/comojugar.png')} alt="Como jugar"  />
                                    <p></p>
                                </div>
                            </div>

                            
                            <div className="col">                
                                <form onSubmit={this.submitHandler} className="form-help">    
                                    <div className="opciones">
                                        <div className="form-group">
                                            <div><label><h5>Idioma</h5></label></div>
                                            <select className="selectidioma" name="language" onChange={this.changeHandler}>
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

                                    <button type="submit" className="btn btn-success" onClick={() => this.setState({visibility: "block"})}><h5>Ayuda 🥺👉👈</h5></button>
                                </form>
                            </div>

                            <div className="col">
                                <div style={{display: this.state.visibility}}>
                                    <div className="solucion" >
                                        <label><h5>Soluciones posibles</h5></label>                              
                                    </div>

                                    {display}

                                    {/* comment here
                                    {this.state.loading &&
                                                <div className="alert alert-secondary" role="alert">
                                                     Cargando
                                                </div>}

                                    {listWords.length > 0 ? (
                                        <ul className="list-group scrollbar-sunny-morning">
                                            {listWords}
                                        </ul>
                                    ) : (
                                        <div className="alert alert-danger" role="alert">
                                            ⚠️No existen coincidencias⚠️
                                        </div>
                                    )}
                                    */}

                                </div>
                            </div>
                        </div>
                    </div>
                
                    <Footer />

            </div>
        );
    }
}