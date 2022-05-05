import {useCallback, useState} from 'react'
import AuthService from '../service/AuthService'

export default function useUser () {
    const [state, setState] = useState({ loading: false, error: false })

    const login = useCallback((username, password) => {
      setState({loading: true, error: false })
      AuthService.loginService(username, password)
      .then((response) => {
        if (response.data.token) {
          localStorage.setItem("token", JSON.stringify(response.data.token));
          localStorage.setItem("username", JSON.stringify(response.data.username));
          localStorage.setItem("userId", JSON.stringify(response.data.userId));
          {/*
           localStorage.setItem("email", JSON.stringify(response.data.email));
           */}
          setState({loading: false, error: false })
        }
      })
      .catch((error) => {
        //TODO ponemos una toast aca?
        //console.log(error.response.data.message);
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        localStorage.removeItem('userId')
        setState({loading: false, error: true })
      });
    })
      
      const logout = () => {
        console.log('logout')
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        localStorage.removeItem('userId')
        setState({loading: false, error: false })
      }
    
      return {
        isLogged: localStorage.getItem("token"),
        isLoginLoading: state.loading,
        hasLoginError: state.error,
        login,
        logout
    }
} 