package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public abstract class AbstractWebMvcTest {

    @Autowired
    protected MockMvc mvc;

    protected static final String AUTHORIZATION_HEADER_NAME = "Authorization";

}
