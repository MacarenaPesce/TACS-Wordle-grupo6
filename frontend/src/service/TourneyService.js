import axios from "axios";
import authHeader from "./AuthHeader";
import AuthService from "./AuthService";

const API_URL = "http://localhost:8080/api/tournaments/";


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

export default {
    createTourney,
    addMember,
    join
};