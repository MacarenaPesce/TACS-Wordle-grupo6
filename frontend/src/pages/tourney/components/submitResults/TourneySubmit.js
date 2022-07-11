import React, { Component } from 'react';
import ReactModal from "react-modal-resizable-draggable";
import UserService from "../../../../service/UserService";
import Not from "../../../../components/not/Not";
import TourneyService from "../../../../service/TourneyService";
import Collapse from "react-bootstrap/Collapse";
import Handler from "../../../../pages/sesion/Handler";

export default class TourneySubmit extends Component{

    constructor(props){
        super(props)
        this.state = {
            modalIsOpen: false,
            availableEnglishResult: true,
            availableSpanishResult: true,
            englishValue: '',
            spanishValue: '',
            language: 'ES',
            punctuation: '',
            loading: false,             //TODO hacer un spinner individual para ingles y español (mover el display del result a otro componente, e instanciarlo dos veces)
            sessionError: false,
            errorMessage: ''
        };

        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
    }

    openModal() {
        this.setState({modalIsOpen: true});
        this.getTodaysResults("EN")
        this.getTodaysResults("ES")
    }

    closeModal() {
        this.setState({modalIsOpen: false});
    }

    changeHandler = (e)  => {
        this.setState({[e.target.name]: e.target.value})
    }

    getTodaysResults(language){
        this.setState({loading: true});
        UserService.getTodaysResult(language)
            .then(response => {
                this.setState({loading: false});
                //console.log(language + ' results: ')
                //console.log(response.data)
                if(response.data.punctuation === 0)
                    this.showMissingValue(language)
                else
                    this.showResultValue(language, response.data.punctuation)
            })
            .catch(error => {
                this.setState({loading: false});
                Handler.handleSessionError(this, error)
            })
    }

    showResultValue(lang, punctuation){
        if(lang === "EN")
            this.setState({englishValue: punctuation, availableEnglishResult: true})
        if(lang === "ES")
            this.setState({spanishValue: punctuation, availableSpanishResult: true})
    }

    showMissingValue(lang){
        const message = "Awaiting today's "+localStorage.getItem("username")+" (id "+localStorage.getItem("userId")+") results to be submitted. For "
        if(lang === "EN")
            this.setState({englishValue: message+"english", availableEnglishResult: false})
        if(lang === "ES")
            this.setState({spanishValue: message+"spanish", availableSpanishResult: false})
        //TODO asignar los valores englishValue y spanishValue con error en el caso de que no responda el back
    }

    submitHandler = e => {
        e.preventDefault()
        this.setState({loading: true});
        //console.log('Boton presionado, se intenta cargar resultados con los datos: ')
        const lang = this.state.language;
        let body = {
            result: this.state.punctuation,
            language: lang,
        }
        //console.log(body)
        TourneyService.submitResults(body)
            .then(response => {
                this.setState({loading: false});
                //console.log('Response del submit obtenida: ')
                //console.log(response.data)
                this.getTodaysResults(lang)
            })
            .catch(error => {
                this.setState({loading: false});
                Handler.handleSessionError(this, error)
            })
    }

    botonClick() {
        if(!this.state.modalIsOpen){
            this.getTodaysResults("EN")
            this.getTodaysResults("ES")
        }
        this.setState({modalIsOpen: !this.state.modalIsOpen})
    }

    render() {
        let englishDisplay
        if (this.state.availableEnglishResult){
            englishDisplay = <p style={{fontSize: "6em"}}>{this.state.englishValue}</p>
        } else {
            englishDisplay =
                        <div className="alert alert-warning" role="alert">
                            {this.state.englishValue}
                        </div>
        }

        let spanishDisplay
        if (this.state.availableSpanishResult){
            spanishDisplay = <p style={{fontSize: "6em"}}>{this.state.spanishValue}</p>
        } else {
            spanishDisplay =
                        <div className="alert alert-warning" role="alert">
                            {this.state.spanishValue}
                        </div>
        }

        let disableButton = false
        if (this.state.availableEnglishResult && (this.state.language === 'EN'))
            disableButton = true
        if (this.state.availableSpanishResult && (this.state.language === 'ES'))
            disableButton = true

        const spinner = (<div className="spinner-border text-dark" role="status">
                            <span className="visually-hidden">Loading...</span>
                        </div>)

        const contenido = ( <div>
                    <h3>Cargar Resultados</h3>

                {/*TODO: hacer css propios en vez de ser tomados de TourneyCreate, para los flexible-modal*/}
                {/*TODO: hacer css propios en vez de ser tomados de help, para: form-help, opciones, selectidioma, form-control*/}
                <form onSubmit={this.submitHandler} className="form-tourney">

                    <div className="opciones">
                        <div className="">
                            <label><h5>Puntaje</h5></label>
                            <input type="text" className="form-control" pattern="[1-7]{1}" title="Puntaje del 1 al 7" required placeholder="Su puntaje obtenido... (no mienta)" name="punctuation" onChange={this.changeHandler} />
                        </div>
                    </div>

                    <div className="opciones">
                        <div className="">
                            <div><label><h5>Idioma</h5></label></div>
                            <select className="selectidioma" name="language" onChange={this.changeHandler}>
                                <option value="ES">Español</option>
                                <option value="EN">English</option>
                            </select>
                        </div>
                    </div>

                    {disableButton ?
                        (<button type="submit" className="btn btn-success" disabled><h5>Cargar resultados</h5></button>) :
                        (<button type="submit" className="btn btn-success" ><h5>Cargar resultados</h5></button>)
                    }

                </form>

                <h3>Resultados de hoy:</h3>

                <div className="row">
                    <div className="col-md-6">
                        <h4>Español</h4>
                        {this.state.loading ? <div>{spinner}</div> :
                            <div>{spanishDisplay}</div>}
                    </div>
                    <div className="col-md-6">
                        <h4>Inglés</h4>
                        {this.state.loading ? <div>{spinner}</div> :
                            <div>{englishDisplay}</div>}
                    </div>

                </div>
            </div>
        )

        return (
            <div className="TourneySubmit">

                {this.state.sessionError &&
                    <Not message={this.state.errorMessage}/>}

                {this.props.modal ?
                    (<div>
                    <button type="submit" className="btn btn-outline-success my-2 my-sm-0" onClick={this.openModal}><h6>Cargar Resultados</h6></button>
                    <ReactModal
                    initWidth={520}
                    initHeight={677}
                    top={200}
                    left={300}
                    onFocus={() => console.log("Modal is clicked")}
                    className={"my-modal-custom-class"}
                    onRequestClose={this.closeModal}
                    isOpen={this.state.modalIsOpen}>

                    {contenido}

                    <button className="btn btn-outline-success my-2 my-sm-0" onClick={this.closeModal}>
                        Cerrar
                    </button>

                    </ReactModal>
                    </div>) :
                    (
                        <div>
                            <button type="button" className="btn btn-outline-success my-2 my-sm-0"
                                    onClick={() => this.botonClick()}
                                    aria-controls="collapse-create"
                                    aria-expanded={this.state.modalIsOpen}
                            >
                                <h6>Cargar Resultados del día</h6>
                            </button>
                            <Collapse in={this.state.modalIsOpen}>
                                <div id="collapse-create">
                                    {contenido}
                                </div>
                            </Collapse>
                        </div>
                    )}
            </div>
        )
    }
}
