import React from 'react';
import { BsTrashFill, BsInfoLg, BsCheckLg, BsPersonPlusFill } from "react-icons/bs";
import { AiOutlineUsergroupAdd, AiOutlineUserAdd } from "react-icons/ai";

function BotonesTorneos(tipoTorneo){
    /*debugger*/
    console.log(tipoTorneo.type)

    if(tipoTorneo.type === "PRIVATE"){
        return(
            <div> 
                <button className="btn btn-danger" type="submit">
                    <BsTrashFill/>
                </button>
                <button className="btn btn-primary" type="submit" href="/info" onClick={console.log("acabas de tocar el boton info")}>
                    <BsInfoLg/> 
                </button>
                <button className="btn btn-warning" type="submit" >
                    <AiOutlineUsergroupAdd/> {/* AiOutlineUsergroupAdd -> agregar personas  */}
                </button>
            </div>
            )
    }
    else if(tipoTorneo.type === 'PUBLIC'){
        return(
            <div>
                <button className="btn btn-danger" type="submit">
                    <BsTrashFill/>
                </button>
                <button className="btn btn-primary" type="submit" href="/info" onClick={console.log("acabas de tocar el boton info")}>
                    <BsInfoLg/> 
                </button>
                <button className="btn btn-info" type="submit">
                    <AiOutlineUserAdd/> {/* AiOutlineUserAdd -> agregarte  */}
                </button>
            </div>
        )
    }
    else{
        return(
            <div>
                <p> no estoy tomando la condicion :)))</p>
                <button className="btn btn-danger" type="submit">
                    <BsTrashFill/>
                </button>
                <button className="btn btn-primary" type="submit">
                    <BsInfoLg/> {/*en info puede ir el creador, el puntaje, el puesto */}
                </button>
            </div>
        )
    }
}

export default BotonesTorneos;


