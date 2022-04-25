import React, { Component , useState} from "react";
import './Not.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import LogoCancel from '../img/icons8-cancel-30.svg'; 

/*import "https://fonts.googleapis.com/css?family=Roboto|Varela+Round";
import "https://fonts.googleapis.com/icon?family=Material+Icons";
import "https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css";
import "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css";
import  "https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js";
import "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js";*/
/*
function Not (){
        return(
            <div>
                {/* todo: poner un model aca de error*//*}
               /*

                <div id="myModal" class="modal fade">
                    <div class="modal-dialog modal-confirm">
                        <div class="modal-content">
                            
                            <div class="modal-body">
                                <p class="text-center">Your transaction has failed. Please go back and try again.</p>
                            </div>
                            <div class="modal-footer">
                                <button class="btn btn-danger btn-block" data-dismiss="modal">OK</button>
                            </div>
                        </div>
                    </div>
                </div>     
            </div>
        );
}

export default Not;*/

function Not() {
    const [show, setShow] = useState(false);
  
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
  
    return (
      <div>
        <Button variant="primary" onClick={handleShow}>
          Launch demo modal - tiene que ser un modal error sin boton
        </Button>
  
        <Modal show={show} onHide={handleClose}>
          <Modal.Header closeButton>                          
            <Modal.Title>Sorry!</Modal.Title>
            <div class="icon-box">
                
            </div>				
          </Modal.Header>
          <Modal.Body>Página no encontrada.</Modal.Body>
          <Modal.Footer>
            <Button variant="primary" href="/">
              Inicio
            </Button>
            <Button variant="secondary" onClick={handleClose}>
              Cerrar
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  }
  
  export default Not;
/*
  render(<Not />);*/