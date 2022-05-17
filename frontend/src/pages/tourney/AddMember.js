import React, { Component } from 'react';
import ReactModal from 'react-modal-resizable-draggable';
import TourneyService from "../../service/TourneyService";
import SessionCheck from "../sesion/SessionCheck";
import Not from "../../components/not/Not";
import { AiOutlineUsergroupAdd, AiOutlineUserAdd } from "react-icons/ai";
import UserService from '../../service/UserService';


export default class TourneyCreate extends Component{

    constructor(){
        super()
        this.state = {
            modalIsOpen: false,
            users: [],
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

    /**revisar */
    changeHandler = (e)  => {
        this.setState({[e.target.name]: e.target.value})
    }

    /**revisar */
    submitHandler = e => {
        e.preventDefault()
        this.setState({errorVisible: false, errorMessage: '', successVisible: false, loading: true});
        console.log('Boton presionado, se intenta crear un torneo con los datos: ')
        /**revisar */
        let body = {
            username: this.state.name,
        }
        UserService.getUsers()
        .then(response => {
                console.log('Response de creación obtenida: ')
                console.log(response.data)
                this.setState({successVisible: true, nameDisplay: this.state.name, loading: false});
            })
            .catch(error => {
                console.log(error)
                this.setState({errorVisible: true, errorMessage: error.response.data.message, loading: false});

                const status = JSON.stringify(error.response.status)
                const message = SessionCheck(status,JSON.stringify(error.response.data.message));
                if(status === "401" || status === "403" || status === "400"){
                    this.setState({sessionError: true, errorMessage: message})
                }
            })
    }

    render(){
        let spinner = (<div className="spinner-border text-black" role="status">
            <span className="visually-hidden">Loading...</span>
        </div>);
        return(

        <div className="TourneyCreate">

            {this.state.sessionError &&
                <Not message={this.state.errorMessage}/>}

            <button type="submit" className="btn btn-warning" onClick={this.openModal}> <AiOutlineUsergroupAdd/> </button>
            <ReactModal
                initWidth={400}
                initHeight={430}
                top={100}
                left={200}
                onFocus={() => console.log("Modal is clicked")}
                className={"my-modal-custom-class"}
                onRequestClose={this.closeModal}
                isOpen={this.state.modalIsOpen}>

                <h3>Crear Torneo</h3>

                {/*TODO: hacer css propios en vez de ser tomados de help, para:
                            form-help, opciones, selectidioma, form-control*/}
                <form onSubmit={this.submitHandler} className="form-help">

                    <div className="opciones">
                        <div className="">
                            <label><h5>Nombre</h5></label>
                            <input type="text" className="form-control" placeholder="Nombre del torneo..." name="username" onChange={this.changeHandler} />
                        </div>
                    </div>

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

                <button className="btn btn-outline-success my-2 my-sm-0" onClick={this.closeModal}>
                    Cerrar
                </button>
            </ReactModal>
        </div>

        );
    }
}