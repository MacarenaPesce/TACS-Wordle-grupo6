import React, { useEffect } from 'react'
import Button from 'react-bootstrap/Button'
import { BsInfoLg } from "react-icons/bs";
import { AiOutlineUserAdd } from "react-icons/ai";
import AddMember from "../addMember/AddMember"
import UserService from "../../../../service/UserService";
import TourneyService from "../../../../service/TourneyService";

export default function BotonesTorneos(data){
    debugger
    let tourney = data.tourney;
    let userId = localStorage.getItem("userId");
    let myTourneysid = [];
    
    console.log("data:",data);
    console.log("id torneo actual", tourney.tourneyId);

    let disableButtonUserAdd = false;
    let disableButtonAddMember = false;

    const clickAgregarme = () => {
        TourneyService.join(tourney.tourneyId);
    }

    const getTourney= () =>{
        UserService.getMyTourneysActive()
            .then(response => {
                myTourneysid = response.data.map((torneo)=>torneo.tourneyId)
                console.log(myTourneysid);
                disableButtons();
            })
            .catch(error => {
                console.log(error)
            })
    }

    const disableButtons= () =>{
        debugger
        console.log("disableButton 1:", disableButtonUserAdd); 

        if(tourney.owner.id == userId){ //es mi torneo
            if(tourney.type === 'PUBLIC' || tourney.type === 'PRIVATE'){ // es publico o privado
                console.log("esta en ready?",tourney.state == 'READY');

                if(tourney.state == 'READY'){  // esta por empezar
                    /* si sos el creador agregas personas ya sea publico o privado SI NO ESTA EMPEZADO*/
                    disableButtonAddMember = false;
                }
                else{
                    // si esta empezado o finalizado se deshabilita el addMember
                    disableButtonAddMember = true;
                }
            }
        }
        else if(tourney.owner.id != userId){ // no es mi torneo
            if(tourney.type === 'PUBLIC'){ // es publico
                /* te permite agregarte al torneo si el torneo es de tipo publico y vos NO sos el creador y se deshabilita si ya estas en el torneo*/
                if(myTourneysid.includes(tourney.tourneyId))
                    disableButtonUserAdd= true;
                else 
                    disableButtonUserAdd= false;
                console.log("disableButton 2:", disableButtonUserAdd); 
            }
            else{ // es privado pero no es mi torneo (me agregaron)
                /*No es mi torneo, no puedo agregar a nadie*/
                disableButtonAddMember = true;
            }
        }
    }
            
    useEffect(() => {
        getTourney();
    }, []);

    return(
        <div>
            <Button className="btn btn-primary" type="button" href={'info/' + tourney.tourneyId}>
                <BsInfoLg/> 
            </Button>

            {disableButtonAddMember ?
                (<p></p>):
                (<AddMember tourneyId ={tourney.tourneyId} ownerId ={tourney.owner.id}/>)
            }

            {disableButtonUserAdd ?
                (<button className="btn btn-info" type="button" disabled><AiOutlineUserAdd/></button>) :
                (<button className="btn btn-info" type="button" onClick={()=>clickAgregarme()}> <AiOutlineUserAdd/> </button>)
            }
        </div>
    )
}