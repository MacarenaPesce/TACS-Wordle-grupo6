import React, { Component , useState} from "react";
import './Toast.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Home from './../../pages/Home'
import Toast from 'react-bootstrap/Toast';
import ToastHeader from 'react-bootstrap/ToastHeader'
import ToastBody from 'react-bootstrap/ToastBody'
import ToastContainer from 'react-bootstrap/ToastContainer'
import { Container, Row, Col } from 'react-bootstrap';

function ToastComponent(props) {

  const [showA, setShowA] = useState(true);

  const toggleShowA = () => setShowA(!showA);

  return (
    <div>
      <Row>
        <Col md={6} className="mb-2">
          <Button onClick={toggleShowA} className="mb-2">
            nombre boton
          </Button>
          <Toast show={showA} onClose={toggleShowA}>
            <Toast.Header>
              <img
                src="holder.js/20x20?text=%20"
                className="rounded me-2"
                alt=""
              />
              <strong className="me-auto">titulo</strong>
            </Toast.Header>
            <Toast.Body>{props.message}</Toast.Body>
          </Toast>
        </Col>
      </Row>      
    </div>
  );
}

export default ToastComponent;