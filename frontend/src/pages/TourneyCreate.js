import React, { Component } from 'react';
import './Tourney.css'


export default class TourneyCreate extends Component{

    constructor(){
        super()
        this.state = {

        }
    }


    render(){

        return(

            <button type="submit" className="btn btn-success"><h6>Crear torneo</h6></button>

        );
    }
}