import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import Home from './pages/Home';
import Help from './pages/Help';
import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
/*import Footer from './components/footer/Footer';*/
import Login from './pages/sesion/Login';
import Register from './pages/sesion/Register';
import {
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="login" element={<Login />} />
      <Route path="register" element={<Register />} />
      <Route path="help" element={<Help />} />
    </Routes>  
  </BrowserRouter>
  
  /*<React.StrictMode>
    <Footer />  
  </React.StrictMode>  */
);

reportWebVitals();