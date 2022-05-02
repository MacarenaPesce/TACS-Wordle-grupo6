import {useCallback, useState} from 'react'
import AuthService from '../service/AuthService'

export default function useUser () {
    const [state, setState] = useState({ loading: false, error: false })

    const login = useCallback((username, password) => {
      setState({loading: true, error: false })
      AuthService.loginService(username, password)
      .then((response) => {
        if (response.data.token) {
          localStorage.setItem("tokenData", JSON.stringify(response.data));
          setState({loading: false, error: false })
        }
      })
      .catch((error) => {
        //TODO ponemos una toast aca?
        //console.log(error.response.data.message);
        localStorage.removeItem('tokenData')
        setState({loading: false, error: true })
      });
    })
      
      const logout = () => {
        console.log('logout')
        localStorage.removeItem('tokenData')
        setState({loading: false, error: false })
      }
    
      return {
        isLogged: Boolean(localStorage.getItem('tokenData')),
        isLoginLoading: state.loading,
        hasLoginError: state.error,
        login,
        logout
    }
} 