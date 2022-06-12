import React, { Component , useState} from "react";
import './NotificationBar.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Button from 'react-bootstrap/Button';

function NotificationBar(props) {

  return (
    <div>
        {/* Info message */}
        <a class="btn btn-info" onclick="toastr.info('Hi! I am info message.');">Info message</a>
        {/* Warning message */}
        <a class="btn btn-warning" onclick="toastr.warning('Hi! I am warning message.');">Warning message</a>
        {/* Success message */}
        <a class="btn btn-success" onclick="toastr.success('Hi! I am success message.');">Success message</a>
        {/* Error message */}
        <a class="btn btn-danger" onclick="toastr.error('Hi! I am error message.');">Error message</a>          
    </div>
  );
}

export default NotificationBar;
 
 
 
 
 
 
