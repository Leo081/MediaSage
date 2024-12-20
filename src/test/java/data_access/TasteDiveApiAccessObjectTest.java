package data_access;

import app.Configurator;
import org.junit.jupiter.api.Test;
import use_case.generate_recommendations.GenDataAccessException;
import use_case.generate_recommendations.GenDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

class TasteDiveApiAccessObjectTest {

    @Test
    void testApi() {
        Configurator configurator = new Configurator();
        GenDataAccessInterface api = new TasteDiveRecommendation();
        api.setApiKey(configurator.getTasteDiveApiKey());
        List<String> base = new ArrayList<>();
        base.add("Alien Romulus");
        base.add("late night with the devil");
        try {
            api.getRecommendation(base, "movie", "movie");
        } catch (GenDataAccessException ex) {
            fail(ex.getMessage());
        }
    }
}
