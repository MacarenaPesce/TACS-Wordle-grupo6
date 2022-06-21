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
