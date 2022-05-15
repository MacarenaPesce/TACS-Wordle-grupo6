import React, { Component } from 'react';
import ReactModal from "react-modal-resizable-draggable";
import UserService from "../../service/UserService";
import Not from "../../components/not/Not";
import TourneyService from "../../service/TourneyService";
import StatusCheck from "../sesion/StatusCheck";
import AuthService from "../../service/AuthService";

export default class TourneySubmit extends Component{

    constructor(){
        super()
        this.state = {
            modalIsOpen: false,
            availableEnglishResult: true,
            availableSpanishResult: true,
            englishValue: '',
            spanishValue: '',
            language: 'ES',
            result: '',
            loading: false,             //TODO hacer un spinner individual para ingles y español
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
                console.log(language + ' results: ')
                console.log(response.data)
                if(language === "EN")
                    this.setState({englishValue: response.data.result, availableEnglishResult: true})
                if(language === "ES")
                    this.setState({spanishValue: response.data.result, availableSpanishResult: true})
            })
            .catch(error => {
                this.setState({loading: false});
                console.log(error)
                if(language === "EN")
                    this.setState({englishValue: error.response.data.message, availableEnglishResult: false})
                if(language === "ES")
                    this.setState({spanishValue: error.response.data.message, availableSpanishResult: false})
                //TODO asignar los valores englishValue y spanishValue con error en el caso de que no responda el back

                this.handleSessionError(error);
            })
    }

    submitHandler = e => {
        e.preventDefault()
        this.setState({loading: true});
        console.log('Boton presionado, se intenta cargar resultados con los datos: ')
        const lang = this.state.language;
        let body = {
            result: this.state.result,
            language: lang,
        }
        console.log(body)
        TourneyService.submitResults(body)
            .then(response => {
                this.setState({loading: false});
                console.log('Response del submit obtenida: ')
                console.log(response.data)
                this.getTodaysResults(lang)
            })
            .catch(error => {
                console.log(error)
                this.setState({loading: false});

                this.handleSessionError(error);
            })
    }

    handleSessionError(error){
        const status = JSON.stringify(error.response.status)
        const message = StatusCheck(status,JSON.stringify(error.response.data.message));
        if(status === "401" || status === "403"){
            AuthService.logout()
            this.setState({sessionError: true, errorMessage: message})
        }
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

        return (
            <div className="TourneySubmit">

                {this.state.sessionError &&
                    <Not message={this.state.errorMessage}/>}

                <button type="submit" className="btn btn-outline-success my-2 my-sm-0" onClick={this.openModal}><h6>Cargar Resultados</h6></button>
                <ReactModal
                    initWidth={520}
                    initHeight={666}
                    top={200}
                    left={300}
                    onFocus={() => console.log("Modal is clicked")}
                    className={"my-modal-custom-class"}
                    onRequestClose={this.closeModal}
                    isOpen={this.state.modalIsOpen}>

                    <h3>Cargar Resultados</h3>

                    {/*TODO: hacer css propios en vez de ser tomados de TourneyCreate, para los flexible-modal*/}
                    {/*TODO: hacer css propios en vez de ser tomados de help, para:
                            form-help, opciones, selectidioma, form-control*/}
                    <form onSubmit={this.submitHandler} className="form-help">

                        <div className="opciones">
                            <div className="">
                                <label><h5>Puntaje</h5></label>
                                <input type="text" className="form-control" pattern="[0-9]{1}" title="Un número" required placeholder="Su puntaje obtenido... (no mienta)" name="result" onChange={this.changeHandler} />
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

                    <button className="btn btn-outline-success my-2 my-sm-0" onClick={this.closeModal}>
                        Cerrar
                    </button>

                </ReactModal>
            </div>
        )
    }



}
