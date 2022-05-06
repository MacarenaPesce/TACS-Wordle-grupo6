import React from 'react';
import NavbarAut from '../../components/navbar/Navbar';
import Footer from '../../components/footer/Footer';
import './Dictionary.css'
import DictionaryService from '../../service/DictionaryService';

export default class Dictionary extends React.Component {

constructor(){
  super()
    this.state = {
        language: 'ES',
        word: '',
        significado: [],
        visibility: "none"
    }
}

manejarCambio = (e)  => {
  this.setState({[e.target.name]: e.target.value})
}

submitCambio = e => {
  e.preventDefault()
  console.log('Boton Search presionado con los datos: ')
  console.log(this.state.word , this.state.language)

  DictionaryService.getDictionary(this.state.word, this.state.language)
      .then(response => {
          console.log('Response obtenida: ')
          console.log(response.data)
          this.setState({significado: response.data.definition})
      })
      .catch(error => {
          console.log(error)
      })
}

render(){ 

    let listDef = this.state.significado.map((defi) => 
    <li class="list-group-item disabled" key={defi}> {defi}</li>)

    return (
      <div className='body-diccionario'>
        <header className="navGeneral">
          <NavbarAut />
        </header>
        <form onSubmit={this.submitCambio}>
           <div className="form-groupo"  >
             <div className='contenedor-busqueda' >
                <input 
                  type="text" 
                  pattern="[A-ZÑa-zñáéíóú]*" 
                  title="Solo letras" 
                  className="buscar-input" 
                  placeholder="Ingrese palabra a buscar"
                  name='word'
                  onChange={this.manejarCambio}
                />
                <button className='boton-busqueda' type="submit" onClick={() => this.setState({significado: []})}>
                  Buscar
                </button>
              </div>
              <div className='contenedor-idioma'>
              <div><label><h5>Idioma</h5></label></div>
              <select className="form-select" name="language" onChange={this.manejarCambio} >
                <option value="ES">Español</option>
                <option value="EN">English</option>
              </select> 
              </div> 
              <div className='contenedor-respuesta'>
                    {listDef}
              </div>  
           </div>
        </form>
        <Footer />
      </div>
    );
  }
}

