import React, { Component } from 'react';
import ReactModal from 'react-modal-resizable-draggable';
import './TourneyCreate.css'
import "./FormAddMember.css"
import UserService from "../../service/UserService";
import StatusCheck from "../sesion/StatusCheck";
import Not from "../../components/not/Not";
import { AiOutlineUsergroupAdd } from "react-icons/ai";

export default class AddMember extends Component  {

    constructor(){
        super()
        let user={
            idUser:'',
            userName:'',
            email:'',
        }
        this.state = {
            modalIsOpen: false,
            users: [user],
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
        console.log('Boton presionado, se intenta buscar los usuarios: ')
        UserService.getUsers()
            .then(response => {
                this.setState({successVisible: true, nameDisplay: this.state.name, loading: false,
                users: response.data.users});
                console.log('Response de usuarios obtenida: ')
                console.log(this.state.users)
           
            })
            .catch(error => {
                console.log(error)
                this.setState({errorVisible: true, errorMessage: error.response.data.message, loading: false});

                const status = JSON.stringify(error.response.status)
                const message = StatusCheck(status,JSON.stringify(error.response.data.message));
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

        <div > 

            {this.state.sessionError &&
                <Not message={this.state.errorMessage}/>}

            <button className= "btn btn-warning"  type="submit"  onClick={this.openModal}><AiOutlineUsergroupAdd/></button>
            <ReactModal
                initWidth={400}
                initHeight={500}
                top={100}
                left={200}
                onFocus={() => console.log("Modal is clicked")}
                className={"my-modal-custom-class"}
                onRequestClose={this.closeModal}
                isOpen={this.state.modalIsOpen}>

                <h3>Add Member</h3>

                {/*TODO: hacer css propios en vez de ser tomados de help, para:
                            form-help, opciones, selectidioma, form-control*/}
                <form onSubmit={this.submitHandler} className="form-help">
                    <div>
                        <button type="submit" className="btn btn-success"><h5>Search</h5></button>
                        <button className="btn btn-outline-success my-2 my-sm-0" onClick={this.closeModal}>
                            X
                        </button>
                    </div>
                    <div className="opciones">
                        <div className="">
                            <label><h5>User Name</h5></label>
                            <input type="text" className="form-control" placeholder="Nombre del usuario.." name="name" onChange={this.changeHandler} />
                        </div>
                    </div>
                    <div className="form-user-list">
                        {this.state.users && this.state.users.length > 0 ? (
                         this.state.users.map((user) => (
                            <li key={user.idUser} className="form-user">
                            <span className="form-user-id">{user.id} </span>
                            <span className="form-user-name">{user.username}</span>
                            </li>
                        ))
                        ) : (
                        <h1>No results found!</h1>
                        )}
                    </div>

                </form>
                {this.state.errorVisible &&
                    <div className="alert alert-danger" role="alert">
                        {this.state.errorMessage}
                    </div>}
                {this.state.loading &&
                    <div className="alert alert-white" role="alert">
                        {spinner}{spinner}
                    </div>}
            </ReactModal>
        </div>

        );
    }
}