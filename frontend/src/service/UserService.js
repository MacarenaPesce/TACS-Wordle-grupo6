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

const getMyTourneys = (nombreTabla, pageNumber, maxResults, name) => {
  if(nombreTabla === 'Mis torneos'){
    return getTourneysState(pageNumber,maxResults,'ACTIVE', name);
  }
  else if(nombreTabla === 'Publicos'){
    return getTourneysStateAndType(pageNumber,maxResults,'READY','PUBLIC',name);
  }
  else{
    return getTourneysState(pageNumber,maxResults,'FINISHED',name);
  }  
};

//es ready o started
const getMyTourneysActive = (pageNumber,maxResults,name) => {
  return getTourneysState(pageNumber,maxResults,'ACTIVE',name);
};

//torneos por tipo (publico o privado)
const getTourneysType = (pageNumber, maxResults, type, name) => {
  return axios.get(API + "tournaments?pageNumber="+ pageNumber + "&maxResults="+ maxResults + "&type=" + type +"&name=" +name, { headers: authHeader() });
};

//torneos por estado (ready - started - finished)
const getTourneysState = (pageNumber, maxResults, state,name) => {
  return axios.get(API + "tournaments?pageNumber="+ pageNumber + "&maxResults="+ maxResults + "&state=" + state +"&name=" +name, { headers: authHeader() });
};

//torneos por estado (ready - started - finished) y tipo
const getTourneysStateAndType = (pageNumber, maxResults, state, type, name) => {
  return axios.get(API + "tournaments?pageNumber=" + pageNumber + "&maxResults=" + maxResults + "&state=" + state  + "&type=" + type +"&name=" +name, { headers: authHeader() });
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
  getTourneysStateAndType,
  getTourneysType
};
