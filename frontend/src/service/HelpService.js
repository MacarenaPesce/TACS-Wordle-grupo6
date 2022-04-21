import axios from "axios";

const API_URL = "http://localhost:8080/api/help/";

export default class HelpService {

    static postHelp(body, lang) {

        return axios.post(API_URL + lang, body);

    }
}