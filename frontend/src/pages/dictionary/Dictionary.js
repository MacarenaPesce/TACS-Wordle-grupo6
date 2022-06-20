import React from 'react';
import NavbarAut from '../../components/navbar/Navbar';
import Footer from '../../components/footer/Footer';
import './Dictionary.css'
import DictionaryService from '../../service/DictionaryService';
import Not from "../../components/not/Not";
import Tourney from "../tourney/Tourney";
import Handler from "../sesion/Handler";

export default class Dictionary extends React.Component {

constructor(){
  super()
    this.state = {
        language: 'ES',
        word: '',
        significado: [],
        visibility: "none",
        loading: false,
        errorMessage: '',
        sessionError: false
    }
}

manejarCambio = (e)  => {
  this.setState({[e.target.name]: e.target.value})
}

submitCambio = e => {
  e.preventDefault()
  this.setState({loading: true})
  console.log('Boton Search presionado con los datos: ')
  console.log(this.state.word , this.state.language)

  DictionaryService.getDictionary(this.state.word, this.state.language)
      .then(response => {
          console.log('Response obtenida: ')
          console.log(response.data)
          this.setState({significado: response.data.definitions, loading:false})
      })
      .catch(error => {
          this.setState({loading: false})
          {/*todo: informar errores modal */}
          console.log(error)
          Handler.handleSessionError(this, error)
      })
}

render(){ 

    let listDef = this.state.significado.map((defi) => 
    <li class="list-group-item disabled" key={defi}> {defi}</li>)
    let spinner = (<div className="spinner-border text-info" role="status">
                    <span className="visually-hidden">Loading...</span>
                </div>);

    return (
      <div className='fondo-diccionario'>
            <header className="navGeneral">
              <NavbarAut />
            </header>
          {this.state.sessionError &&
              <Not message={this.state.errorMessage}/>}
          <div className='body-diccionario'>
            <form onSubmit={this.submitCambio}>
               <div className="form-groupo"  >
                 <div className='contenedor-busqueda' >
                    <input
                      type="text"
                      pattern="[A-ZÑa-źñáéíóú]*"
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
                   {this.state.loading ? (
                       <div className='contenedor-respuesta scrollbar-lady-lips'>
                           {spinner}{spinner}{spinner}{spinner}{spinner}
                       </div>
                   ) : (
                       <div className='contenedor-respuesta scrollbar-lady-lips'>
                           {listDef}
                       </div>
                   )}
               </div>
            </form>
          </div>
          <Footer />
      </div>
    );
  }
}

