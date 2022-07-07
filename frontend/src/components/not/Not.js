import React, { useState} from "react";
import './Not.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Home from './../../pages/Home'


function Not(prop) {
    const [show, setShow] = useState(true);
  
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
  
    return (
      <div>
        <Home />

        <Modal show={show} onHide={handleShow}>
          <div className="modalErrorPage">
            <Modal.Header closeButton>                          
              <Modal.Title>Sorry!</Modal.Title>				
            </Modal.Header>
            <Modal.Body>{prop.message}</Modal.Body>
            <Modal.Footer className="modal-footer">
              {/*
              <Button variant="primary" href="/">
                Inicio
              </Button>
              */}
                <Button className="button-modal" variant="danger" onClick={handleClose} href="/">
                  Ok
                </Button>
            </Modal.Footer>
          </div>
        </Modal>
      </div>
    );
  }
  
export default Not;
