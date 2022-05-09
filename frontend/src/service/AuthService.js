import axios from "axios";

const API_URL = "http://localhost:8080/api/auth/";

const registerService = (username, email, password) => {
  return axios.post(API_URL + "register", {
    username,
    email,
    password,
  });
};

const loginService = (username, password) => {
  return axios
    .post(API_URL + "login", {
      username,
      password,
    });
};
const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  localStorage.removeItem('userId')

};

export default {
  registerService,
  loginService,
  logout,
};