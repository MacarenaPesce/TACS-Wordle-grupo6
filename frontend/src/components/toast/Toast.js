import React, { useState} from "react";
import './Toast.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Toast from 'react-bootstrap/Toast';

function ToastComponent(props) {

  const [showA, setShowA] = useState(true);

  const toggleShowA = () => setShowA(!showA);

  return (
    <div>    
      <Toast show={showA} onClose={toggleShowA}>
        <Toast.Header>
          <img
            src="holder.js/20x20?text=%20"
            className="rounded me-2"
            alt=""
          />
          <strong className="me-auto">{props.tittle}Titulo</strong>
        </Toast.Header>
        <Toast.Body>{props.message}</Toast.Body>
      </Toast>
    </div>
  );
}

export default ToastComponent;