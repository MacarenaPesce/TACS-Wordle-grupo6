import {useCallback, useState} from 'react'
import AuthService from '../service/AuthService'
import { useNavigate } from 'react-router-dom';

export default function useUser () {
    const [state, setState] = useState({ loading: false, error: false })
    const navigate = useNavigate();

    const login = useCallback((username, password) => {
      setState({loading: true, error: false })
      AuthService.loginService(username, password)
      .then((response) => {
        console.log('Response obtenida: ')
        console.log(response.data)
        if (response.data.token) {
          localStorage.setItem("tokenData", JSON.stringify(response.data));
          setState({loading: false, error: false })
        }
      })
      .catch((error) => {
        //TODO ponemos una toast aca?
        //console.log(error.response.data.message);
        console.log(error)
        localStorage.removeItem('tokenData')
        setState({loading: false, error: true })
      });
    })

    const register = useCallback((username,email,password) => {
      setState({loading: true, error: false})
      AuthService.registerService(username, email, password)
      .then(response => {
        console.log('Response obtenida: ')
        console.log(response.data)
        if (response.data.token) {
          localStorage.setItem("tokenData", JSON.stringify(response.data));
          setState({loading: false, error: false })
        }
      })
      .catch(error => {
          console.log(error)
          localStorage.removeItem('tokenData')
          setState({loading: false, error: true })
      });
    })
      
    const logout = () => {
      console.log('logout')
      localStorage.removeItem('tokenData')
      setState({loading: false, error: false })
      navigate('/')
    }
    
      return {
        isLogged: localStorage.getItem("tokenData"),
        isLoginLoading: state.loading,
        hasLoginError: state.error,
        login,
        register,
        logout
    }
} 