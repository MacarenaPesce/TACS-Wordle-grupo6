import React, { useEffect, useState} from 'react'
import Button from 'react-bootstrap/Button'
import { BsInfoLg } from "react-icons/bs";
import { AiOutlineUserAdd } from "react-icons/ai";
import AddMember from "../addMember/AddMember"
import TourneyService from "../../../../service/TourneyService";
import UserService from '../../../../service/UserService';

export default function BotonesTorneos(data){
    const [tourney,setTourney] = useState(data.tourney);
    const [myTourneysid, setTourneysId] = useState([]); 

    let userId = localStorage.getItem("userId");

    const [disableButtonUserAdd,setDisButUserAdd] = useState(false);
    const [disableButtonAddMember,setDisButAddMember] = useState(false);

    const clickAgregarme = () => {
        TourneyService.join(tourney.tourneyId);
    }

    const disableButtons= () =>{
        if(tourney.owner.id == userId){ //es mi torneo
            if(tourney.state === 'READY'){  // esta por empezar
                setDisButAddMember(false); //si sos el creador agregas personas ya sea publico o privado SI NO ESTA EMPEZADO
            }
            else{
                setDisButAddMember(true); // si esta empezado o finalizado se deshabilita el addMember
            }
            setDisButUserAdd(true); //no puedo agregarme porque es mi torneo
        }
        else if(tourney.owner.id != userId){
            setDisButAddMember(true); //No es mi torneo, no puedo agregar a nadie
            if(tourney.state === 'READY' && tourney.type === 'PUBLIC'){ // esta en ready y no empezado, y es publico
                if(myTourneysid.includes(tourney.tourneyId)){
                    setDisButUserAdd(true);  //si ya esta en mis torneos lo deshabilito
                }
                else{
                    setDisButUserAdd(false); //si no estoy incluido lo dejo disponible para agregarme
                }                        
            }
            else{ 
                setDisButUserAdd(true); // es privado pero no es mi torneo (me agregaron)
            }
        }  
    }

    const getTourneysId = () =>{
        UserService.getMyTourneysActiveForId() //todo: hacer esta funcion en back
        .then(response => {
            let ids = response.data.tournaments.map((torneo)=>torneo.tourneyId)
            setTourneysId(ids);
        })
        .catch(error => {
            console.log(error)
        })
    }

    function loadData(){
        getTourneysId();
        disableButtons();
    }

    useEffect(() => {
        loadData();
    }, []);

    return(
        <div>
            <Button className="btn btn-primary" type="button" href={'info/' + tourney.tourneyId}>
                <BsInfoLg/> 
            </Button>

            {disableButtonUserAdd ?
                (<div></div>):
                (<button className="btn btn-info" type="button" onClick={()=>clickAgregarme()}> <AiOutlineUserAdd/> </button>)
            }

            {disableButtonAddMember ?
                (<div></div>):
                (<AddMember tourneyId ={tourney.tourneyId} ownerId ={tourney.owner.id}/>)
            }

        </div>
    )
}