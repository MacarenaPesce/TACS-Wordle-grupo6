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

const getMyTourneys = (nombreTabla, pageNumber, maxResults) => {
  if(nombreTabla === 'Mis torneos'){
    return getMyTourneysActive(pageNumber,maxResults);
  }
  else if(nombreTabla === 'Publicos'){
    return getTourneysPublic(pageNumber,maxResults);
  }
  else{
    return getTourneysFinished(pageNumber,maxResults);
  }  
};

const getMyTourneysActive = (pageNumber,maxResults) => {
  return axios.get(API + "tournaments/myTournaments"+'?'+"pageNumber="+pageNumber+ "&maxResults="+maxResults, { headers: authHeader() });
};

const getTourneysPublic = (pageNumber,maxResults) => {
  return axios.get(API + "tournaments/public"+'?'+"pageNumber="+pageNumber+ "&maxResults="+maxResults, { headers: authHeader() });
};

const getTourneysFinished = (pageNumber,maxResults) => {
  return axios.get(API + "tournaments/FINISHED"+'?'+"pageNumber="+pageNumber+ "&maxResults="+maxResults, { headers: authHeader() });
};

const getMyTourneysActiveForId = () => {
  return axios.get(API + "tournaments/myTournaments", { headers: authHeader() });
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
  getTodaysResult,
  getMyTourneysActiveForId
};
