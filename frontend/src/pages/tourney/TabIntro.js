import TourneyCreate from './TourneyCreate'
import React from 'react';

export default function TabIntro(){
    return(
        <div class="container">
            <div class="row">
                <div class="col-md-3">
                    <form className="form-inline" >
                        <input className="form-control " type="search" placeholder="Ingrese el nombre del torneo" aria-label="Search" />
                    </form>
                </div>
                <div class="col-md-3">
                    <form className="form-inline" >
                        <button className="btn btn-outline-success my-2 my-sm-0" type="submit">Buscar</button>
                    </form>
                </div>
                <div class="col-md-3">
                    <form className="form-inline" >
                        <button className="btn btn-outline-success my-2 my-sm-0" type="submit">Actualizar</button>
                    </form>
                </div>
                <div class="col-md-3"> {/*todo: poner esta columna a la derecha */}
                    <TourneyCreate />
                </div>
            </div>
        </div>
    )
}