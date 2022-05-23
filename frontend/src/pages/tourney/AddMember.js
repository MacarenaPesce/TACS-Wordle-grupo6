import React, { Component } from 'react';
import ReactModal from 'react-modal-resizable-draggable';
import "./AddMember.css"
import UserService from "../../service/UserService";
import StatusCheck from "../sesion/StatusCheck";
import Not from "../../components/not/Not";
import { AiOutlineUsergroupAdd } from "react-icons/ai";
import Member from './Member.js';
import TourneyService from '../../service/TourneyService';
import AuthService from "../../service/AuthService";

export default class AddMember extends Component  {

    constructor(props){
        super(props)
        let user={
            id:'',
            username:'',
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
            clear: true,
            searchUser:'',
        };

        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
    }

    openModal() {
        this.setState({modalIsOpen: true});
    }
    closeModal() {
        this.setState({modalIsOpen: false});
        this.setState({clear:true})
    }


    changeHandler = (e)  => {
        this.setState({searchUser: e.target.value});
        console.log(this.state.searchUser);
    }

		toAdd = (tourneyid,id) =>{
				console.log('lamando a toAdd con userId: '+ id+ "torneo: " + tourneyid);
				this.setState({errorVisible: false, errorMessage: '', successVisible: false, loading: true});
				TourneyService.addMember(tourneyid,id)
					.then(response => {
						this.setState({successVisible: true, nameDisplay: this.state.name, loading: false})
						console.log("se agregó el usuario: "+id+ " al torneo 1")
						console.log(response)
					})
					.catch(error => {   //todo marca bloque catch de codigo duplicado con TourneyCreate
						console.log(error)
						this.setState({errorVisible: true, errorMessage: error.response.data.message, loading: false});
						const status = JSON.stringify(error.response.status)
						const message = StatusCheck(status,JSON.stringify(error.response.data.message));
						if(status === "401" || status === "403"){
                            AuthService.logout()
							this.setState({sessionError: true, errorMessage: message})
						}
				})
		}

    submitHandler = e => {
        e.preventDefault()
        this.setState({errorVisible: false, errorMessage: '', successVisible: false, loading: true});
        UserService.getUsers(this.state.searchUser)
            .then(response => {console.log(response.data)
                this.setState({successVisible: true, nameDisplay: this.state.name, loading: false,
                users: response.data});
                console.log('Response de usuarios obtenida: ')
                console.log(this.state.users)
                this.setState({clear:false})
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

        <React.Fragment>

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
                <form onSubmit={this.submitHandler} className="form-container">
                    <div>
                        <button type="submit" className="btn btn-success"><h5>Search</h5></button>
                        <button className="btn btn-outline-success my-2 my-sm-0" onClick={this.closeModal}>
                            X
                        </button>
                    </div>
                    <div className="opciones">
                        <div className="">
                            <label><h5>User Name</h5></label>
                            <input type="text" className="form-control" placeholder="Nombre del usuario.." searchUser="searchUser" onChange={this.changeHandler} />
                        </div>
                    </div>
                    <div className="form-user-list">
                    
                        { !this.state.clear ? (
                          this.state.users && this.state.users.length > 0 ? (
                           this.state.users.map((user) => 
                            <Member
															 id={user.id}
															 username={user.username}
                                                             tourneyid ={this.props.tourneyId}
															 toAdd={this.toAdd} 
															 />
                        )
                        ) : (
                        <h1>No results found!</h1>
                        )): (<div></div>)
                    }
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
        </React.Fragment>

        );
    }
}
