package data_access;

import app.Configurator;
import org.junit.Test;
import use_case.note.DataAccessException;
import use_case.generate_recommendations.GenDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TasteDiveApiAccessObjectTest {

    @Test
    public void testApi() {
        Configurator configurator = new Configurator();
        GenDataAccessInterface api = new TasteDiveRecommendation();
        api.setApiKey(configurator.getTasteDiveApiKey());
        List<String> base = new ArrayList<>();
        base.add("Alien Romulus");
        base.add("late night with the devil");
        try {
            api.getRecommendation(base, "movie", "movie");
        } catch (DataAccessException ex) {
            fail(ex.getMessage());
        }
    }
}
