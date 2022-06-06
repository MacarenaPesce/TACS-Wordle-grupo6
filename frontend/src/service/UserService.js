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
    return getMyTourneysActive();
  }
  else if(nombreTabla === 'Publicos'){
    return getTourneysPublic();
  }
  else{
    return getTourneysFinished();
  }  
};

const getTodaysResult = (language) => {
  return axios.get(API + "punctuation/todaysResult/" + language, { headers: authHeader() });
};

const getMyTourneysActive = () => {
  return axios.get(API + "tournaments/myTournaments", { headers: authHeader() });
};

const getTourneysPublic = () => {
  return axios.get(API + "tournaments/public", { headers: authHeader() });
};

const getTourneysFinished = () => {
  return axios.get(API + "tournaments/FINISHED", { headers: authHeader() });
};

const getUsers = (userSearch) => {
  return axios.get(API_USERS +'?'+ 'username='+userSearch, { headers: authHeader() });
};

export default {
  getPublicContent,
  getUserBoard,
  getModeratorBoard,
  getAdminBoard,
  getMyTourneys,
  getMyTourneysActive,
  getTourneysPublic,
  getUsers,
  getTodaysResult
};
