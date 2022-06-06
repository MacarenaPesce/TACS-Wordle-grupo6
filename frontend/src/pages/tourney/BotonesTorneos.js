import React from "react";
import Button from 'react-bootstrap/Button'
import { BsTrashFill, BsInfoLg, BsCheckLg, BsPersonPlusFill } from "react-icons/bs";
import { AiOutlineUsergroupAdd, AiOutlineUserAdd } from "react-icons/ai";
import { HiLogout } from "react-icons/hi";
import TourneyService from '../../service/TourneyService';
import AddMember from "./AddMember"
import UserService from "../../service/UserService";
import {GoCheck} from "react-icons/go";



function BotonesTorneos(data){
    let tourney = data.tourney;
    let userId = localStorage.getItem("userId");
    console.log("id torneo actual", tourney.tourneyId);
    console.log("dataTourneys", data.dataTourneys);

    let disableButton = false;
    console.log(disableButton);
    
    if(data.dataTourneys.includes(tourney.tourneyId))
        disableButton= true;
    else 
        disableButton= false;

    console.log(disableButton);    
    
    const clickAgregarme = () => {
        console.log("te agregaste a un torneo". tourney)
        TourneyService.join(tourney.tourneyId);
    }


    if(tourney.owner.id == userId){ //es mi torneo
        if(tourney.type === 'PUBLIC' || tourney.type === 'PRIVATE'){ // es publico o privado
            if(tourney.state!= 'FINISHED'){  // no esta finalizado
                return( //tabla mis torneos
                    <div> 
                        {/* como soy el creador lo puedo eliminar*/}
                        {/** 
                        <button className="btn btn-danger" type="button">
                            <BsTrashFill/> 
                        </button>
                        */}
                        <Button className="btn btn-primary" type="button" href={'info/' + tourney.tourneyId} onClick={()=>console.log("acabas de tocar el boton info")}>
                            <BsInfoLg/> 
                        </Button>
                        {/* si sos el creador agregas personas ya sea publico o privado */}
                        <AddMember tourneyId ={tourney.tourneyId} 
                                     ownerId ={tourney.owner.id}/> 
                    </div>
                )
            }
            else{ // esta finalizado
                return(
                    <div>
                        <Button className="btn btn-primary" type="button" href={'info/' + tourney.tourneyId} onClick={()=>console.log("acabas de tocar el boton info")}>
                            <BsInfoLg/> 
                        </Button>
                    </div>  )                      
            }
        }
    }
    else if(tourney.owner.id != userId){ // no es mi torneo
        if(tourney.type === 'PUBLIC'){ // es publico
            return(
                <div>
                        {/* si no soy el creador, me puedo salir */}
                    {/*
                    <button className="btn btn-danger" type="button">
                        <HiLogout/> 
                    </button>*/}
                    <Button className="btn btn-primary" type="button" href={'info/' + tourney.tourneyId} onClick={()=>console.log("acabas de tocar el boton info")}>
                        <BsInfoLg/> 
                    </Button>

                    {/* te permite agregarte al torneo si el torneo es de tipo publico y vos NO sos el creador 
                     y se deshabilita si ya estas en el torneo*/}

                    {disableButton ?
                            (<button className="btn btn-info" type="button" disabled><AiOutlineUserAdd/></button>) :
                            (<button className="btn btn-info" type="button" onClick={()=>clickAgregarme()}> <AiOutlineUserAdd/> </button>)
                    }
                </div>
            )
        }
        else{ // es privado pero no es mi torneo (me agregaron)
            return(
                <div>
                    {/* si no soy el creador, me puedo salir */}
                    {/*<button className="btn btn-danger" type="button">
                        <HiLogout/> 
                    </button>*/}
                    <Button className="btn btn-primary" type="button" href={'info/' + tourney.tourneyId} onClick={()=>console.log("acabas de tocar el boton info")}>
                        <BsInfoLg/> 
                    </Button>
                </div>                
            )
        }
    }
    /*
    else{
        return(
            <div>
                <p> no estoy tomando la condicion :))) (aca tendria que ir la condicion de finalizados)</p>
                <button className="btn btn-primary" type="button">
                    <BsInfoLg/>
                </button>
            </div>
        )
    }*/
}

export default BotonesTorneos;


