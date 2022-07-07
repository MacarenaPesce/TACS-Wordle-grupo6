import axios from "axios";
import authHeader from "./AuthHeader";

const API_URL = process.env.REACT_APP_API_URL + "test/";
const API_USERS = process.env.REACT_APP_API_URL + "users/";
const API = process.env.REACT_APP_API_URL + "";

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

const getTodaysResult = (language) => {
  return axios.get(API + "punctuation/todaysResult/" + language, { headers: authHeader() });
};

const getUsers = (userSearch) => {
  return axios.get(API_USERS + '?username=' + userSearch, { headers: authHeader() });
};

//id de todos los torneos de un usuario
const getMyTourneysId = () => {
  return axios.get(API + "tournaments/ids", { headers: authHeader() } );
};

//Generico sin paginacion para todos los torneos
const getMyTourneysGeneric = () => {
  return axios.get(API + "tournaments", { headers: authHeader() });
};

const getMyTourneys = (nombreTabla, pageNumber, maxResults) => {
  if(nombreTabla === 'Mis torneos'){
    return getTourneysState(pageNumber,maxResults,'ACTIVE');
  }
  else if(nombreTabla === 'Publicos'){
    return getTourneysStateAndType(pageNumber,maxResults,'READY','PUBLIC');
  }
  else{
    return getTourneysState(pageNumber,maxResults,'FINISHED');
  }  
};

//es ready o started
const getMyTourneysActive = (pageNumber,maxResults) => {
  return getTourneysState(pageNumber,maxResults,'ACTIVE');
};


//torneos por estado (ready - started - finished)
const getTourneysState = (pageNumber, maxResults, state) => {
  return axios.get(API + "tournaments?pageNumber=" + pageNumber + "&maxResults=" + maxResults + "&state=" + state, { headers: authHeader() });
};

//torneos por estado (ready - started - finished) y tipo
const getTourneysStateAndType = (pageNumber, maxResults, state, type) => {
  return axios.get(API + "tournaments?pageNumber=" + pageNumber + "&maxResults=" + maxResults + "&state=" + state  + "&type=" + type, { headers: authHeader() });
};

/* eslint import/no-anonymous-default-export: [2, {"allowObject": true}] */
export default {
  getPublicContent,
  getUserBoard,
  getModeratorBoard,
  getAdminBoard,
  getMyTourneys,
  getMyTourneysActive,
  getUsers,
  getTodaysResult,
  getMyTourneysGeneric,
  getMyTourneysId,
  getTourneysStateAndType
};
