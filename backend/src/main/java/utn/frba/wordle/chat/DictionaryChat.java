package utn.frba.wordle.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.TeleSender;

@Service
public class DictionaryChat {
    //funcion de obtener definiciones de palabras en español y en ingles

    @Autowired
    TeleSender sender;
}
