import axios from "axios";
import authHeader from "./AuthHeader";
import AuthService from "./AuthService";

const API_URL = "http://localhost:8080/api/tournaments/";
const API_PUNCTUATION = "http://localhost:8080/api/punctuation/";


const createTourney = (body) => {

    /*const token = authHeader()
    const tokenString = JSON.stringify(token.Authorization)
    if(tokenString === undefined){
        AuthService.logout()
    }*/ //duplicado ya hace esto en sessionCheck

    return axios.post(API_URL, body,{ headers: authHeader() });
};

const addMember = (tournamentId, userId) => {
    return axios.post(API_URL+tournamentId+"/members/"+userId, {}, { headers: authHeader() });
};

const join = (tournamentId) => {
    return axios.post(API_URL+tournamentId+"/join", {}, { headers: authHeader() });
};

const submitResults = (body) => {
    return axios.post(API_URL + "submitResults", body, { headers: authHeader() });
};

const getTournamentFromId = (tournamentId) => {
    return axios.get(API_URL+"info/"+tournamentId, { headers: authHeader() });
};

const getRanking = (tournamentId) => {
    return axios.get(API_URL+tournamentId+"/ranking", { headers: authHeader() });
};

const getEndOfTheDay = () => {
    return axios.get(API_PUNCTUATION + "endOfTheDay", { headers: authHeader() });
};

const getDayOfTheDate = () => {
    return axios.get(API_PUNCTUATION + "dayOfTheDate", { headers: authHeader() });
};

const getUsersTournament = (tournamentId) => {
    return axios.get(API_URL+tournamentId+"/members", { headers: authHeader() });
}

const getPunctuation = (tournamentId, userId) => {
    return axios.get(API_URL+tournamentId+"/punctuation/"+userId, { headers: authHeader() });
}

const getPosition = (tournamentId, userId) => {
    return axios.get(API_URL+tournamentId+"/position/"+userId, { headers: authHeader() });
}

export default {
    submitResults,
    createTourney,
    addMember,
    join,
    getTournamentFromId,
    getRanking,
    getEndOfTheDay,
    getDayOfTheDate,
    getUsersTournament,
    getPunctuation,
    getPosition
};
