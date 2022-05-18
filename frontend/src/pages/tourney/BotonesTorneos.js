import React, { useState } from "react";
import Button from 'react-bootstrap/Button'
import { BsTrashFill, BsInfoLg, BsCheckLg, BsPersonPlusFill } from "react-icons/bs";
import { AiOutlineUsergroupAdd, AiOutlineUserAdd } from "react-icons/ai";
import { HiLogout } from "react-icons/hi";
import TourneyService from '../../service/TourneyService';
import UserService from "../../service/UserService";
import AddMember from "./AddMember"


function BotonesTorneos(tourney){
    let userId = localStorage.getItem("userId");

    const clickAgregarme = () => {
        console.log("te agregaste a un torneo". tourney.torneo)
        TourneyService.join(tourney.torneo.tourneyId);
    }

    if(tourney.torneo.owner.id == userId){
        if(tourney.torneo.type === 'PUBLIC' || tourney.torneo.type === 'PRIVATE'){
            return(
                <div> 
                    {/* como soy el creador lo puedo eliminar*/}
                    {/** 
                    <button className="btn btn-danger" type="button">
                        <BsTrashFill/> 
                    </button>
                    */}
                    <Button className="btn btn-primary" type="button" href="/info" onClick={()=>console.log("acabas de tocar el boton info")}>
                        <BsInfoLg/> 
                    </Button>
                    <AddMember tourneyId ={tourney.torneo.tourneyId} /> {/*si sos el creador agregas personas ya sea publico o privado */}
                </div>
            )
        }
    }
    else if(tourney.torneo.owner.id != userId){
        if(tourney.torneo.type === 'PUBLIC'){
            return(
                <div> {/*es un torneo publico al que entraste o te agregaron y no sos el creador */}
                        {/* si no soy el creador, me puedo salir */}
                    {/*
                    <button className="btn btn-danger" type="button">
                        <HiLogout/> 
                    </button>*/}
                    <Button className="btn btn-primary" type="button" href="/info" onClick={()=>console.log("acabas de tocar el boton info")}>
                        <BsInfoLg/> 
                    </Button>
                    <button className="btn btn-info" type="button" onClick={()=>clickAgregarme()}>
                        <AiOutlineUserAdd/> {/* te permite agregarte al torneo si el torneo es de tipo publico y vos NO sos el creador 
                                            todo:falta validar que no estes en la lista de integrantes del torneo*/}
                    </button>
                </div>
            )
        }
        else{
            return(
                <div> {/* es un torneo privado (al que te agregaron) y no sos el creador*/}
                    {/* si no soy el creador, me puedo salir */}
                    {/*
                    <button className="btn btn-danger" type="button">
                        <HiLogout/> 
                    </button>*/}
                    <Button className="btn btn-primary" type="button" href="/info" onClick={()=>console.log("acabas de tocar el boton info")}>
                        <BsInfoLg/> 
                    </Button>
                </div>                
            )
        }
    }
    else{
        return(
            <div>
                <p> no estoy tomando la condicion :))) (aca tendria que ir la condicion de finalizados)</p>
                <button className="btn btn-primary" type="button">
                    <BsInfoLg/>
                </button>
            </div>
        )
    }
}

export default BotonesTorneos;


