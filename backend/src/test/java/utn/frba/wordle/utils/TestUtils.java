package utn.frba.wordle.utils;

import com.google.gson.Gson;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class TestUtils {

    public static final EasyRandom RANDOM = TestUtils.newEasyRandom();

    private static EasyRandom newEasyRandom() {
        final EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.setRandomizationDepth(3);
        return new EasyRandom(parameters);
    }

    public static String toJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}