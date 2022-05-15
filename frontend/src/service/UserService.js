import axios from "axios";
import authHeader from "./AuthHeader";

const API_URL = "http://localhost:8080/api/test/";
const API_USERS = "http://localhost:8080/api/users/";
const API = "http://localhost:8080/api/";

const getPublicContent = () => {
  return axios.get(API_URL + "all");
};

const getUserBoard = () => {
  return axios.get(API_URL + "user", { headers: authHeader() });
};

const getModeratorBoard = () => {
  return axios.get(API_URL + "mod", { headers: authHeader() });
};

const getAdminBoard = () => {
  return axios.get(API_URL + "admin", { headers: authHeader() });
};

const getMyTourneys = (nombreTabla) => {
  if(nombreTabla === 'Mis torneos'){
    let userId = localStorage.getItem("userId");
    return axios.get(API_USERS + userId + "/tournaments", { headers: authHeader() });
  }
  else if(nombreTabla === 'Publicos'){
    console.log('estas en torneos publicos')
    return getTourneysPublic();
  }
  else{
    console.log('falta la tabla de finalizados :) ')
  }
  
};

const getTourneysPublic = () => {
  return axios.get(API + "tournaments/public", { headers: authHeader() });
};

const getUsers = () => {
  return axios.get(API_USERS, { headers: authHeader() });
};

export default {
  getPublicContent,
  getUserBoard,
  getModeratorBoard,
  getAdminBoard,
  getMyTourneys,
  getTourneysPublic,
  getUsers
};
