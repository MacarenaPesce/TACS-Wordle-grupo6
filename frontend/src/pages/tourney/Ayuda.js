import {AiOutlineCrown, AiOutlineUserAdd, AiOutlineUsergroupAdd} from "react-icons/ai";
import {BsInfoLg, BsTrashFill} from "react-icons/bs";
import {GoCheck} from "react-icons/go";
import React from "react";


const ayudaMis = <div className="alert alert-primary" role="alert">
    <p>
        <h4><p>Mis torneos: Te salen torneos en los que estas inscripto</p></h4>

        <h4><p>Botones:</p></h4>

        <button className="btn btn-primary" type="submit" href="/info">
            <BsInfoLg/>
        </button> Info del torneo + Ranking
        
        <p></p>

        {/*<h4><p>Te salen tus torneos creados:</p></h4>*/}

        <button className="btn btn-warning" type="submit" >
            <AiOutlineUsergroupAdd/>
        </button> Boton para sumar personas
        {/*<button className="btn btn-danger" type="submit">
            <BsTrashFill/>
        </button> Finalizar torneo ya o eliminarlo (extra)*/}
        <p></p>

        {/*<h4><p>Te salen torneos de otros a los que estas anotado:</p></h4>*/}

        <button className="btn btn-info" type="submit">
            <AiOutlineUserAdd/>
        </button> Este botón es para sumarse a un torneo publico y se muestra deshabilitado si ya estas en el torneo
        {/*<button className="btn btn-danger" type="submit">
            <BsTrashFill/>
        </button> Salir del torneo (extra) (dejas de estar anotado)*/}
    </p>
</div>

const ayudaPublic =     <div className="alert alert-primary" role="alert">
    <p>
        <h4><p>Publicos: Te salen torneos publicos.</p></h4>
        
        <h4><p>Botones:</p></h4>

        <button className="btn btn-primary" type="submit" href="/info">
            <BsInfoLg/>
        </button> Info del torneo + Ranking
        
        <p></p>

        <button className="btn btn-info" type="submit">
            <AiOutlineUserAdd/>
        </button> Boton para sumarte (solo si aún no estás participando)
        
        <p></p>

        {/*
        <button className="btn btn-danger" type="submit">
            <BsTrashFill/>
        </button> Salir del torneo (extra) (solo si estas participando)
        <p></p>*/}

        {/*<h4><p>Te salen tus torneos publicos creados:</p></h4>*/}
        <button className="btn btn-warning" type="submit" >
            <AiOutlineUsergroupAdd/>
        </button> Boton para sumar personas (solo si sos creador)
        
        <p></p>
        
        {/*
        <button className="btn btn-danger" type="submit">
            <BsTrashFill/>
        </button> Finalizar torneo ya (extra)*/}

    </p>
</div>

const ayudaFin = <div className="alert alert-primary" role="alert">
    <p>
        <h4><p>Torneos finalizados de los cuales hayas participado/jugado:</p></h4>
        <button className="btn btn-primary" type="submit" href="/info">
            <BsInfoLg/>
        </button> Info + Ranking actual/final del torneo (no difiere de todos los botones de ranking)
        <p></p>
        {/* todo: falta hacer esto:
        <button className="btn btn-warning" type="submit" href="/info">
            <AiOutlineCrown/>
        </button> Mostrar el ganador en una columna con este icono a su lado*/}
    </p>
</div>


export default {
    ayudaMis,
    ayudaPublic,
    ayudaFin
};