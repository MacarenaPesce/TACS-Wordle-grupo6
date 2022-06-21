import axios from "axios";

const API_URL = process.env.REACT_APP_API_URL + "help/";

export default class HelpService {

    static postHelp(body, lang) {

        return axios.post(API_URL + lang, body);

    }
}