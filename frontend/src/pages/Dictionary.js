import React from 'react';
import NavbarAut from '../components/navbar/Navbar';
import Footer from '../components/footer/Footer';
import './Dictionary.css'
import DictionaryService from '../service/DictionaryService';

class Dictionary extends React.Component {

constructor(){
  super()
    this.state = {
        language: 'ES',
        word: '',
        signifacdo: ''
    }
}

manejarCambio = (e)  => {
  this.setState({[e.target.name]: e.target.value})
}

submitCambio = e => {
  e.preventDefault()
  console.log('Boton Search presionado con los datos: ')
  console.log(this.state.word , this.state.language)

  DictionaryService.postDictionary(this.state.word, this.state.language)
      .then(response => {
          console.log('Response obtenida: ')
          console.log(response.data)
          this.setState({signifacdo: response.data.translate})
      })
      .catch(error => {
          console.log(error)
      })
}

render(){
    return (
      <body className="dictionary" >
        <header>
          <NavbarAut />
        </header>
        <form onSubmit={this.submitCambio}  >
           <div className="form-group"  >
             <div className='contenedor-busqueda' >
                <input 
                  type="text" 
                  pattern="[A-Za-z]*" 
                  title="Solo letras" 
                  className="buscar-input" 
                  placeholder="Ingrese palabra a buscar"
                  name='word'
                  onChange={this.manejarCambio}
                />
                <button className='boton-busqueda' type="submit" >
                  SEARH
                </button>
              </div>
              <div className='contenedor-idioma'>
              <div><label><h5>Idioma</h5></label></div>
              <select name="language" onChange={this.manejarCambio} >
                <option value="ES">Español</option>
                <option value="EN">English</option>
              </select> 
              </div>   
           </div>
        </form>
        <Footer />
      </body>
    );
  }
}


export default Dictionary;