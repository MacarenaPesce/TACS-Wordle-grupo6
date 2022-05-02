import {useCallback, useContext, useState} from 'react'
import Context from '../context/UserContext'
import AuthService from '../service/AuthService'

export default function useUser () {
    const {jwt, setJWT} = useContext(Context)
    const [state, setState] = useState({ loading: false, error: false })

    const login = useCallback((username, password) => {
      console.log('username');
      console.log(username);
      console.log('password');
      console.log(password);
      setState({loading: true, error: false })
      AuthService.loginService(username, password)
      .then((response) => {
        console.log(response.data);
        if (response.data.token) {
          localStorage.setItem("tokenData", JSON.stringify(response.data));
          window.sessionStorage.setItem('jwt', jwt)
          setState({loading: false, error: false })
          setJWT(response.data)
        }
      })
      .catch((error) => {
        console.log(error)
        //TODO ponemos una toast aca?
        window.sessionStorage.removeItem('jwt')
        //console.log(error.response.data.message);
        localStorage.removeItem('tokenData')
        setState({loading: false, error: true })
      });
    }, [setJWT])
      
      const logout = useCallback(() => {
        window.sessionStorage.removeItem('jwt')
        setJWT(null)
      }, [setJWT])
    
      return {
        isLogged: Boolean(jwt),
        isLoginLoading: state.loading,
        hasLoginError: state.error,
        login,
        logout
    }
} 