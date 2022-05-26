package utn.frba.wordle.controller;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import utn.frba.wordle.logging.WordleLogger;

public abstract class AbstractWebMvcTest {

    @Autowired
    protected MockMvc mvc;

    protected static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    @BeforeAll
    public static void setUp(){
        WordleLogger.enableDebug(true);
    }

}
