package utn.frba.wordle.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class WordleLogger {

    @Value("${logback.logging.level.debug}")
    private static Boolean enableDebug;

    public static void enableDebug(Boolean enableDebug){
        WordleLogger.enableDebug = enableDebug;
    }

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = (Logger) LoggerFactory.getLogger(clazz);
        if(enableDebug) {
            logger.setLevel(Level.DEBUG);
        }
        return logger;
    }
}
