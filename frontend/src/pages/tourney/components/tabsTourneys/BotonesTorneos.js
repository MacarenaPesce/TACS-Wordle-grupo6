import React, { useEffect, useState} from 'react'
import Button from 'react-bootstrap/Button'
import { BsInfoLg } from "react-icons/bs";
import { AiOutlineUserAdd } from "react-icons/ai";
import AddMember from "../addMember/AddMember"
import TourneyService from "../../../../service/TourneyService";
import UserService from '../../../../service/UserService';

export default function BotonesTorneos(data){
    //debugger
    const [tourney,setTourney] = useState({owner: ""});
    const [myTourneysid, setTourneysId] = useState([]); 

    let userId = localStorage.getItem("userId");

    const [disableButtonUserAdd,setDisButUserAdd] = useState(false);
    const [disableButtonAddMember,setDisButAddMember] = useState(false);

    const clickAgregarme = () => {
        TourneyService.join(tourney.tourneyId);
    }

    const disableButtons= (tourney) =>{
        if(tourney.type === 'PUBLIC' || tourney.type === 'PRIVATE'){ // es publico o privado
            if(tourney.owner.id == userId){ //es mi torneo
                if(tourney.state == 'READY'){  // esta por empezar
                    setDisButAddMember(false); //si sos el creador agregas personas ya sea publico o privado SI NO ESTA EMPEZADO
                }
                else{
                    setDisButAddMember(true); // si esta empezado o finalizado se deshabilita el addMember
                }
                setDisButUserAdd(true); //no puedo agregarme porque es mi torneo
            }
            else if(tourney.owner.id != userId){ // no es mi torneo
                if(tourney.type === 'PUBLIC'){ // es publico
                    /* te permite agregarte al torneo si el torneo es de tipo publico y vos NO sos el creador y se deshabilita si ya estas en el torneo*/
                    if(myTourneysid.includes(tourney.tourneyId)){
                        setDisButUserAdd(true);  //si ya esta en mis torneos lo deshabilito
                    }
                    else{
                        setDisButUserAdd(false); //si no estoy incluido lo dejo disponible para agregarme
                    }                        
                }
                else{ // es privado pero no es mi torneo (me agregaron)
                    /*No es mi torneo, no puedo agregar a nadie*/
                    setDisButUserAdd(true);
                }
                setDisButAddMember(true);
            }
        }
        else{ //esta finalizado 
            setDisButAddMember(true);
            setDisButUserAdd(true);
        }    
    }

    const getTourneysId = () =>{
        UserService.getMyTourneysActive()
        .then(response => {
            let ids = response.data.tournaments.map((torneo)=>torneo.tourneyId)
            setTourneysId(ids);
            console.log("Torneos id: ", ids);
            //console.log(myTourneysid.includes(tourney.tourneyId));
        })
        .catch(error => {
            console.log(error)
        })
    }

    function loadData(){
        console.log("tourney para el disable: ",data.tourney);
        getTourneysId();  //todo esto no te va responder a tiempo, te conviene pasarle la lista como prop directo de TabsTourney.js. Haciendo eso ademas, te ahorras tener que hacer 1000 request en el caso de tener 1000 torneos en la tabla, reducis a 1

        console.log("Torneo: ", data.tourney.name);

        disableButtons(data.tourney);
        setTourney(data.tourney);
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
                (<button className="btn btn-info" type="button" disabled><AiOutlineUserAdd/></button>) :
                (<button className="btn btn-info" type="button" onClick={()=>clickAgregarme()}> <AiOutlineUserAdd/> </button>)
            }

            {disableButtonAddMember ?
                (<div></div>):
                (<AddMember tourneyId ={tourney.tourneyId} ownerId ={tourney.owner.id}/>)
            }

        </div>
    )
}