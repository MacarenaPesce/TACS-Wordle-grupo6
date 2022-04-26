import axios from "axios";

const API_URL = "http://localhost:8080/api/dictionary/";

export default class DictionaryService {


    static postDictionary(word, lang) {

        return axios.post(API_URL + lang + '/' + word);

    }

    static getDictionary(word, lang) {

        return axios.get(API_URL + lang + '/' + word);

    }
}