package utn.frba.wordle.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.TeleSender;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.enums.TournamentType;
import utn.frba.wordle.service.TournamentService;
import utn.frba.wordle.service.UserService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


@Service
public class TournamentChat {
    //cargar mis resultados del dia, y poder revisar si ya habia cargado
    //poder crear un torneo
    //poder unir usuarios a mis torneos
    //poder consultar lista de mis torneos creados
    //poder consultar lista de los torneos a los que estoy unido
    //poder consultar lista de torneos publicos a los que puedo unirme
    //poder unirme a un torneo publico
    //poder consultar lista torneos publicos empezados
    //poder obtener informacion de un torneo
    //poder consultar el ranking de un torneo started o finalized
    //poder consultar lista de torneos finalizados

    @Autowired
    TeleSender sender;

    @Autowired
    UserService userService;

    @Autowired
    TournamentService tournamentService;

    final HashMap<Long, Integer> pasoActual = new HashMap<>();

    final HashMap<Long, TournamentDto> torneo = new HashMap<>();


    public void processAddMember(Long chat_id, String message, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        Long userid = userService.findUseridByTelegramID(chat_id);
        if(userid == null){
            sender.sendMessage("Debe estar registrado para usar esta función - /register", chat_id, "");
            return;
        }

        if (restart) {
            casoActual.put(chat_id, "addmember");
            pasoActual.put(chat_id, 1);
        }

        Integer step = pasoActual.get(chat_id);
        switch (step) {
            case 1:
                //pedir id torneo a agregar
                pasoActual.put(chat_id, 2);
                sender.sendMessage("Inserte el id del torneo al que quiere agregar un usuario", chat_id, "");
                break;

            case 2:
                //verificar que se recibió un id
                if (!message.matches("\\d+")) {
                    sender.sendMessage("Envie algo que tenga reminiscencia a un id", chat_id, "");
                    return;
                }

                //verificar que exista el torneo activo? //todo pedir aca al service un torneo ready directamente
                Long tourneyid = Long.parseLong(message);
                TournamentDto tourney;
                try {
                    tourney = tournamentService.getActiveTournamentById(tourneyid);
                } catch (BusinessException e) {
                    sender.sendMessage("No existe torneo activo con ese id, elija otro id", chat_id, "");
                    return;
                }
                torneo.put(chat_id, tourney);

                //verificar que el torneo sea propio
                if (!tourney.getOwner().getId().equals(userid)) {
                    sender.sendMessage("Usted no es propietario del torneo elegido, elija otro torneo", chat_id, "");
                    return;
                }

                //verificar que el torneo no este empezado
                if (!tourney.getState().equals(State.READY)) {
                    sender.sendMessage("El torneo al que intenta agregar a un usuario, ya esta empezado, elija otro torneo", chat_id, "");
                    return;
                }

                pasoActual.put(chat_id, 3);
                sender.sendMessage("Inserte el id del usuario al que quiere agregar", chat_id, "");
                break;

            case 3: //Agregarlo si aun no lo está. Notificar la situación
                //verificar que se recibió un id
                if (!message.matches("\\d+")) {
                    sender.sendMessage("Envie algo que tenga reminiscencia a un id", chat_id, "");
                    return;
                }

                //verificar  que aun no este en el torneo
                TournamentDto tournament = torneo.get(chat_id);
                Long id_new_user = Long.parseLong(message);

                if (userService.memberOfTournament(id_new_user, tournament.getTourneyId())) {
                    sender.sendMessage("El usuario ya forma parte del torneo, elija otro id", chat_id, "");
                    return;
                }

                //agregar al torneo
                //dar la opcion de seguir agregando miembros al mismo torneo, o finalizar
                tournamentService.addMember(id_new_user, tournament.getTourneyId(), tournament.getOwner().getId());
                sender.sendMessage(id_new_user+" agregado 👍 - Ingrese más o /exit", chat_id, "");


                break;

            default:
                sender.sendMessage("Disculpame pero no te entendi", chat_id, "");
        }
    }

    public void processJoin(Long chat_id, String message, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        Long userid = userService.findUseridByTelegramID(chat_id);
        if(userid == null){
            sender.sendMessage("Debe estar registrado para usar esta función - /register", chat_id, "");
            return;
        }

        if (restart) {
            casoActual.put(chat_id, "join");
            pasoActual.put(chat_id, 1);
        }

        Integer step = pasoActual.get(chat_id);
        switch (step) {
            case 1:
                //pedir id torneo a agregar
                pasoActual.put(chat_id, 2);
                sender.sendMessage("Inserte el id del torneo al que quiere ingresar", chat_id, "");
                break;

            case 2:
                //verificar que se recibió un id
                if (!message.matches("\\d+")) {
                    sender.sendMessage("Envie algo que tenga reminiscencia a un id", chat_id, "");
                    return;
                }

                //verificar que exista el torneo activo? //todo pedir aca al service un torneo ready directamente
                Long tourneyid = Long.parseLong(message);
                TournamentDto tourney;
                try {
                    tourney = tournamentService.getActiveTournamentById(tourneyid);
                } catch (BusinessException e) {
                    sender.sendMessage("No existe torneo activo con ese id, elija otro id", chat_id, "");
                    return;
                }

                //verificar que el torneo no este empezado
                if (!tourney.getState().equals(State.READY)) {
                    sender.sendMessage("El torneo al que intenta agregarse, ya esta empezado, elija otro torneo", chat_id, "");
                    return;
                }

                //verificar que el torneo sea publico
                if (tourney.getType().equals(TournamentType.PRIVATE)) {
                    sender.sendMessage("El torneo al que intenta agregarse no es publico, elija otro torneo", chat_id, "");
                    return;
                }

                //verificar que aun no este participando del torneo
                if (userService.memberOfTournament(userid, tourneyid)) {
                    sender.sendMessage("Usted ya forma parte de ese torneo, elija otro id", chat_id, "");
                    return;
                }

                //agregarse al torneo

                tournamentService.join(userid, tourneyid);
                sender.sendMessage("Ahora participa de "+tourneyid+" 👍 \nElija mas torneos o /exit", chat_id, "");

                break;

            default:
                sender.sendMessage("Disculpame pero no te entendi", chat_id, "");

        }
    }

    //TODO: FALTA AGREGAR EL EXIT EN CUALQUIER PASO POR SI QUIERE SALIR
    public void processCreateTourney(Long chat_id, String[] params, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException{
        Long userid = userService.findUseridByTelegramID(chat_id);
        if(userid == null){
            sender.sendMessage("Debe estar registrado para usar esta función - /register", chat_id, "");
            return;
        }
        if (restart) {
            casoActual.put(chat_id, "create");
            pasoActual.put(chat_id, 1);
        }

        Integer step = pasoActual.get(chat_id);
        switch (step) {
            case 1:
                torneo.put(chat_id, TournamentDto.builder().build());
                String sugerencia = UUID.randomUUID().toString();

                //pedir nombre, verificar que no exista previamente
                pasoActual.put(chat_id, 2);
                sender.sendMessageWithKB("Inserte el nombre del torneo que quiere crear", chat_id, "", "[[\""+sugerencia+"\"]]");
                break;

            case 2:
                String nombre = String.join(" ", params);
                //verifico que el nombre no exista previamente
                if( !tournamentService.activeTournamentExists(nombre) ){
                    TournamentDto new_tourney = torneo.get(chat_id);
                    new_tourney.setName(nombre);
                    torneo.put(chat_id, new_tourney);
                    pasoActual.put(chat_id, 3);
                    //pedir idioma
                    sender.sendMessageWithKB("Ingrese idioma del torneo: ES (español) - EN (Ingles)", chat_id, "", "[[\"ES\",\"EN\"]]");
                }else {
                    sender.sendMessage("El torneo ya existe, elija otro nombre", chat_id, "");
                    return;
                }

                break;

            case 3:
                //verifico que sea un idioma existente
                try {
                    Language lang = Language.valueOf(params[0].toUpperCase());
                    //guardar idioma
                    TournamentDto new_tourney = torneo.get(chat_id);
                    new_tourney.setLanguage(lang);
                    torneo.put(chat_id, new_tourney);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("No existe ese lenguaje, elija otro", chat_id, "");
                    return;
                }

                pasoActual.put(chat_id, 4);
                //pedir privacidad
                sender.sendMessageWithKB("Ingrese tipo el tipo: PUBLIC - PRIVATE", chat_id, "", "[[\"Public\",\"Private\"]]");
                break;

            case 4:
                //verifico que sea una privacidad posible
                try {
                    TournamentType tipo = TournamentType.valueOf(params[0].toUpperCase());
                    //guardar tipo
                    TournamentDto new_tourney = torneo.get(chat_id);
                    new_tourney.setType(tipo);
                    torneo.put(chat_id, new_tourney);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("No existe ese tipo, elija otro", chat_id, "");
                    return;
                }

                //fecha de mañana para sugerir
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date tomorrow = calendar.getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy MM dd");
                String strDate = dateFormat.format(tomorrow);

                pasoActual.put(chat_id, 5);
                //pedir fecha de inicio, todo: opcionalmente verificar que sea a partir de mañana.
                sender.sendMessageWithKB("Ingrese fecha de inicio: yyyy MM dd", chat_id, "", "[[\""+strDate+"\"]]");

                break;

            case 5:
                String msg_inicio = String.join(" ", params);
                //verificar formato de fecha inicio
                try {
                    Date inicio = new SimpleDateFormat("yyyy MM dd").parse(msg_inicio);
                    //guardar fecha
                    TournamentDto new_tourney = torneo.get(chat_id);
                    new_tourney.setStart(inicio);
                    torneo.put(chat_id, new_tourney);
                } catch (ParseException e) {
                    sender.sendMessage("Ingresaste mal la fecha, vuelva a intentar", chat_id, "");
                    return;
                }

                //fecha en una semana para sugerir
                Calendar calendar2 = Calendar.getInstance();
                calendar2.add(Calendar.DAY_OF_YEAR, 7);
                Date fecha = calendar2.getTime();
                DateFormat dateFormat2 = new SimpleDateFormat("yyyy MM dd");
                String strDate2 = dateFormat2.format(fecha);

                pasoActual.put(chat_id, 6);
                //pedir fecha fin
                sender.sendMessageWithKB("Ingrese fecha de fin: yyyy MM dd", chat_id, "", "[[\""+strDate2+"\"]]");
                break;

            case 6:
                //todo: verificar que sea mayor o igual a la de inicio.
                String msg_fin = String.join(" ", params);
                TournamentDto new_tourney = torneo.get(chat_id);
                //verificar formato de fecha fin
                try {
                    Date fin = new SimpleDateFormat("yyyy MM dd").parse(msg_fin);
                    new_tourney.setFinish(fin);
                } catch (ParseException e) {
                    sender.sendMessage("Ingresaste mal la fecha, vuelva a intentar", chat_id, "");
                    return;
                }

                //crear torneo y notificar exito
                tournamentService.create(new_tourney, userid);

                sender.sendMessageDelKB("Ya se creo el torneo, tenga buena semana", chat_id, "");

                torneo.remove(chat_id, new_tourney);
                casoActual.remove(chat_id, "create");
                break;

            default:
                sender.sendMessage("Disculpame pero no te entendi", chat_id, "");
        }

    }

}
