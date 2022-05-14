import React from 'react';
import { BsTrashFill, BsInfoLg, BsCheckLg, BsPersonPlusFill } from "react-icons/bs";
import { AiOutlineUsergroupAdd, AiOutlineUserAdd } from "react-icons/ai";
import { HiLogout } from "react-icons/hi";

function BotonesTorneos(tourney){
    /*debugger
    console.log("Estoy en debb")
    console.log(tourney.torneo.name)*/
    /*console.log(torneo.type)*/
    let userId = localStorage.getItem("userId");
    /*console.log(userId);
    console.log(tourney.torneo.owner.id)
    console.log(userId==tourney.torneo.owner.id , userId!=tourney.torneo.owner.id )*/

    if(tourney.torneo.owner.id == userId){
        if(tourney.torneo.type === 'PUBLIC' || tourney.torneo.type === 'PRIVATE'){
            return(
                <div> 
                    <button className="btn btn-danger" type="submit">
                        <BsTrashFill/> {/* como soy el creador lo puedo eliminar*/}
                    </button>
                    <button className="btn btn-primary" type="submit" href="/info" onClick={console.log("acabas de tocar el boton info")}>
                        <BsInfoLg/> 
                    </button>
                    <button className="btn btn-warning" type="submit" >
                        <AiOutlineUsergroupAdd/> {/*si sos el creador agregas personas ya sea publico o privado */}
                    </button>
                </div>
            )
        }
    }
    else if(tourney.torneo.owner.id != userId){
        if(tourney.torneo.type === 'PUBLIC'){
            return(
                <div> {/*es un torneo publico al que entraste o te agregaron y no sos el creador */}
                    <button className="btn btn-danger" type="submit">
                        <HiLogout/> {/* si no soy el creador, me puedo salir */}
                    </button>
                    <button className="btn btn-primary" type="submit" href="/info" onClick={console.log("acabas de tocar el boton info")}>
                        <BsInfoLg/> 
                    </button>
                    <button className="btn btn-info" type="submit" >
                        <AiOutlineUserAdd/> {/* te permite agregarte al torneo si el torneo es de tipo publico y vos NO sos el creador 
                                            todo:falta validar que no estes en la lista de integrantes del torneo*/}
                    </button>
                </div>
            )
        }
        else{
            return(
                <div> {/* es un torneo privado (al que te agregaron) y no sos el creador*/}
                    <button className="btn btn-danger" type="submit">
                        <HiLogout/> {/* si no soy el creador, me puedo salir */}
                    </button>
                    <button className="btn btn-primary" type="submit" href="/info" onClick={console.log("acabas de tocar el boton info")}>
                        <BsInfoLg/> 
                    </button>
                </div>                
            )
        }
    }
    else{
        return(
            <div>
                <p> no estoy tomando la condicion :))) (aca tendria que ir la condicion de finalizados)</p>
                <button className="btn btn-primary" type="submit">
                    <BsInfoLg/>
                </button>
            </div>
        )
    }
}

export default BotonesTorneos;


