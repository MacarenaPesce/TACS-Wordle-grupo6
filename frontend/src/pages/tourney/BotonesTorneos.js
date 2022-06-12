import React, { useEffect } from 'react'
import Button from 'react-bootstrap/Button'
import { BsInfoLg } from "react-icons/bs";
import { AiOutlineUserAdd } from "react-icons/ai";
import AddMember from "./AddMember"
import UserService from "../../service/UserService";
import TourneyService from "../../service/TourneyService";


function BotonesTorneos(data){
    let tourney = data.tourney;
    let userId = localStorage.getItem("userId");
    
    console.log("data:",data);
    console.log("id torneo actual", tourney.tourneyId);

    let disableButton = false;
    console.log("disable button 1:", disableButton);

    let myTourneysid = [];

    const getTourney= async () =>{
        await UserService.getMyTourneysActive()
            .then(response => {
                console.log("data de torneos activos del user:",response.data)
                myTourneysid = response.data.map((torneo)=>torneo.tourneyId)
                console.log("mis torneos id:", myTourneysid);

                if(myTourneysid.includes(tourney.tourneyId))
                    disableButton= true;
                else 
                    disableButton= false;
        
                console.log("disableButton 2:", disableButton); 
                console.log("include:", myTourneysid.includes(tourney.tourneyId))

            })
            .catch(error => {
                console.log(error)
            })
    }
            
    useEffect(() => {
        getTourney();
    }, []);

    const clickAgregarme = () => {
        console.log("te agregaste a un torneo". tourney)
        TourneyService.join(tourney.tourneyId);
    }


    if(tourney.owner.id == userId){ //es mi torneo
        if(tourney.type === 'PUBLIC' || tourney.type === 'PRIVATE'){ // es publico o privado
            if(tourney.state == 'READY'){  // esta por empezar
                return(
                    <div> 
                        <Button className="btn btn-primary" type="button" href={'info/' + tourney.tourneyId} onClick={()=>console.log("acabas de tocar el boton info")}>
                            <BsInfoLg/> 
                        </Button>
                        {/* si sos el creador agregas personas ya sea publico o privado SI NO ESTA EMPEZADO*/}
                        <AddMember tourneyId ={tourney.tourneyId} 
                                     ownerId ={tourney.owner.id}/> 
                    </div>
                )
            }
            if(tourney.state == 'STARTED'){  // esta empezado
                return( 
                    <div> 
                        <Button className="btn btn-primary" type="button" href={'info/' + tourney.tourneyId} onClick={()=>console.log("acabas de tocar el boton info")}>
                            <BsInfoLg/> 
                        </Button>
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
            console.log("el disable puto",disableButton);
            return(
                <div>
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
                    <Button className="btn btn-primary" type="button" href={'info/' + tourney.tourneyId} onClick={()=>console.log("acabas de tocar el boton info")}>
                        <BsInfoLg/> 
                    </Button>
                </div>                
            )
        }
    }
}

export default BotonesTorneos;


