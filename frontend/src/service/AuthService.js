import axios from "axios";
import authHeader from "./AuthHeader";

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
const ping = () => {
  return axios.get(API_URL+"ping", { headers: authHeader() });
};

export default {
  registerService,
  loginService,
  logout,
  ping
};