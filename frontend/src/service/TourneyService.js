import axios from "axios";
import authHeader from "./AuthHeader";

const API_URL = process.env.REACT_APP_API_URL + "tournaments/";
const API_PUNCTUATION = process.env.REACT_APP_API_URL + "punctuation/";

const createTourney = (body) => {

    /*const token = authHeader()
    const tokenString = JSON.stringify(token.Authorization)
    if(tokenString === undefined){
        AuthService.logout()
    }*/ //duplicado ya hace esto en sessionCheck

    return axios.post(API_URL, body,{ headers: authHeader() });
};

const addMember = (tournamentId, userId) => {
    return axios.post(API_URL + tournamentId + "/members/"+userId, {}, { headers: authHeader() });
};

const join = (tournamentId) => {
    return axios.post(API_URL + tournamentId+"/join", {}, { headers: authHeader() });
};

const submitResults = (body) => {
    return axios.post(API_URL + "submitResults", body, { headers: authHeader() });
};

const getTournamentFromId = (tournamentId) => {
    return axios.get(API_URL + "info/" + tournamentId, { headers: authHeader() });
};

const getRanking = (tournamentId, pageNumber, maxResults) => {
    return axios.get(API_URL + tournamentId + "/ranking?pageNumber=" + pageNumber + "&maxResults=" + maxResults, { headers: authHeader() });
};

const getEndOfTheDay = () => {
    return axios.get(API_PUNCTUATION + "endOfTheDay", { headers: authHeader() });
};

const getDayOfTheDate = () => {
    return axios.get(API_PUNCTUATION + "dayOfTheDate", { headers: authHeader() });
};

const getMembers = (tournamentId) => {
    return axios.get(API_URL + tournamentId + "/members", { headers: authHeader() });
}

const getMyScore = (tournamentId) => {
    return axios.get(API_URL + tournamentId + "/ranking/myScore", { headers: authHeader() })
}

/* eslint import/no-anonymous-default-export: [2, {"allowObject": true}] */
export default {
    submitResults,
    createTourney,
    addMember,
    join,
    getTournamentFromId,
    getRanking,
    getEndOfTheDay,
    getDayOfTheDate,
    getMembers,
    getMyScore
};
