import axios from "axios";
import authHeader from "./AuthHeader";

const API_URL = "http://localhost:8080/api/tournaments/";


const createTourney = (body) => {

    return axios.post(API_URL, body,{ headers: authHeader() });
};

export default {
    createTourney

};