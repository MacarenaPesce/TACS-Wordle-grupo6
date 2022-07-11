import React from "react";
import './NotificationBar.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Toaster, toast} from 'react-hot-toast';

function NotificationBar() {

  return (
      <Toaster 
        position="top-right"
        reverseOrder={true}
        toastOptions={{
          duration: 3000,
          style: {
            borderRadius: '10px',
            background: '#333',
            color: '#fff',
          }
        }}/>          
  );
}

export default NotificationBar;

/* Pasos: 
  1- importar en el archivo js:
  import {Toaster, toast} from 'react-hot-toast';
  2- colocar el toast en el boton donde suceden los cambios:
  onClick....
  3- Colocar debajo <NotificationBar /> e importarlo (mirar ejemplo de login)

Tipos de toast
Confirmacion -> toast.success('mensaje');
Error -> toast.error('mensaje');
Cargando con spinner -> toast.loading('mensaje');
Con iconos -> toast('Buen Trabajo!', { icon: '👏', });

Pagina donde hay mas info: https://react-hot-toast.com/
*/