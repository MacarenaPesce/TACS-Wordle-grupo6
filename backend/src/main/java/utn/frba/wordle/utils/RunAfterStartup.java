package utn.frba.wordle.utils;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class RunAfterStartup {

    public static String TOKEN;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        System.out.println("Yaaah, I am running........");
        try
        {
            TOKEN = Files.readAllLines(Paths.get("botToken.txt")).get(0);
            System.out.println("botToken.txt cargado");
        }
        catch (IOException e)
        {
            TOKEN = "";
            System.out.println("No existe botToken.txt");
        }
    }
}
