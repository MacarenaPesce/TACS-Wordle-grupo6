import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import Home from './pages/Home';
import Help from './pages/help/Help';
import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import Login from './pages/sesion/Login';
import Register from './pages/sesion/Register';
import Not from './components/not/Not'
import {
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";
import Dictionary from './pages/dictionary/Dictionary';
import Tourney from './pages/tourney/Tourney';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="login" element={<Login />} />
      <Route path="register" element={<Register />} />
      <Route path="help" element={<Help />} />
      <Route path="dictionary" element={<Dictionary />} />
      <Route path="tourney" element={<Tourney />} /> 
      <Route path="*" element={<Not />} />
    </Routes>  
  </BrowserRouter>
  
  /*<React.StrictMode>
    <Footer />  
  </React.StrictMode>  */
);

reportWebVitals();