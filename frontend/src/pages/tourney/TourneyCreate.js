import React, { Component } from 'react';
import ReactModal from 'react-modal-resizable-draggable';
import './TourneyCreate.css'
import TourneyService from "../../service/TourneyService";
import StatusCheck from "../sesion/StatusCheck";
import Not from "../../components/not/Not";
import AuthService from "../../service/AuthService";
import Tourney from "./Tourney";
import Ayuda from "./Ayuda";
import Button from "react-bootstrap/Button";
import Collapse from "react-bootstrap/Collapse";

export default class TourneyCreate extends Component{

    constructor(props){
        super(props)
        this.state = {
            modalIsOpen: false,
            name: '',
            type: 'PUBLIC',
            start: '',
            finish: '',
            language: 'ES',
            errorMessage: '',
            errorVisible: false,
            successVisible: false,
            loading: false,
            nameDisplay: '',
            sessionError: false,
        };

        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
    }

    openModal() {
        this.setState({modalIsOpen: true});
    }
    closeModal() {
        this.setState({modalIsOpen: false});
    }


    changeHandler = (e)  => {
        this.setState({[e.target.name]: e.target.value})
    }

    submitHandler = e => {
        e.preventDefault()
        this.setState({errorVisible: false, errorMessage: '', successVisible: false, loading: true});
        console.log('Boton presionado, se intenta crear un torneo con los datos: ')
        let body = {
            name: this.state.name,
            type: this.state.type,
            start: this.state.start,
            finish: this.state.finish,
            language: this.state.language
        }
        console.log(body)
        TourneyService.createTourney(body)
            .then(response => {
                this.setState({successVisible: true, nameDisplay: this.state.name, loading: false});
                console.log('Response de creación obtenida: ')
                console.log(response.data)
            })
            .catch(error => {
                console.log(error)
                this.setState({errorVisible: true, errorMessage: error.response.data.message, loading: false});

                Tourney.handleSessionError(this, error)
            })
    }

    render(){
        let spinner = (<div className="spinner-border text-black" role="status">
            <span className="visually-hidden">Loading...</span>
        </div>);

        const contenido = (     <div>
                <h3>Crear Torneo</h3>

                {/*TODO: hacer css propios en vez de ser tomados de help, para:
                            form-help, opciones, selectidioma, form-control*/}
                <form onSubmit={this.submitHandler} className="form-help">

                    <div className="opciones">
                        <div className="">
                            <label><h5>Nombre</h5></label>
                            <input type="text" className="form-control" placeholder="Nombre del torneo..." name="name" onChange={this.changeHandler} />
                        </div>
                    </div>

                    <div className="opciones">
                        <div className="">
                            <div><label><h5>Tipo</h5></label></div>
                            <select className="selectidioma" name="type" onChange={this.changeHandler}>
                                <option value="PUBLIC">Público</option>
                                <option value="PRIVATE">Privado</option>
                            </select>
                        </div>
                    </div>

                    <div className="opciones">
                        <div className="">
                            <label><h5>Inicio</h5></label>
                            <input type="date" min={this.props.min} className="form-control" placeholder="HRS" name="start" onChange={this.changeHandler} />
                        </div>
                    </div>

                    <div className="opciones">
                        <div className="">
                            <label><h5>Fin</h5></label>
                            <input type="date" min={this.props.min} className="form-control" placeholder="HRS" name="finish" onChange={this.changeHandler} />
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

                    <button type="submit" className="btn btn-success"><h5>Crear Torneo</h5></button>
                </form>
                {this.state.errorVisible &&
                    <div className="alert alert-danger" role="alert">
                        {this.state.errorMessage}
                    </div>}
                {this.state.successVisible &&
                    <div className="alert alert-success" role="alert">
                        Torneo '{this.state.nameDisplay}' creado, actualice la lista manualmente
                    </div>}
                {this.state.loading &&
                    <div className="alert alert-white" role="alert">
                        {spinner}{spinner}
                    </div>}
                </div>
        )

        return(

        <div className="TourneyCreate">

            {this.state.sessionError &&
                <Not message={this.state.errorMessage}/>}

            {this.props.modal ?
                (<div>
                <button type="button" className="btn btn-outline-success my-2 my-sm-0" onClick={this.openModal}><h6>Crear torneo</h6></button>
                <ReactModal
                initWidth={400}
                initHeight={830}
                top={100}
                left={200}
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
                            onClick={() => this.setState({modalIsOpen: !this.state.modalIsOpen})}
                            aria-controls="collapse-create"
                            aria-expanded={this.state.modalIsOpen}
                        >
                            <h6>Crear torneo</h6>
                        </button>
                        <Collapse in={this.state.modalIsOpen}>
                            <div id="collapse-create">
                                {contenido}
                            </div>
                        </Collapse>
                    </div>
                )}

        </div>

        );
    }
}