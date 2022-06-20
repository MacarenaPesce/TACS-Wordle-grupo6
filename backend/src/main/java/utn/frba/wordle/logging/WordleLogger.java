package utn.frba.wordle.logging;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class WordleLogger {

    public static Logger getLogger(Class<?> clazz) {
        return (Logger) LoggerFactory.getLogger(clazz);
    }
}
