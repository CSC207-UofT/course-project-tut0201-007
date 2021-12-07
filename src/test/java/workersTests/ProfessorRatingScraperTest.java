package workersTests;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.junit.Before;
import org.junit.Test;
import util.RateMyProfessorException;
import workers.ProfessorRatingScraper;

public class ProfessorRatingScraperTest {
    JsonObject test1, test2;

    private JsonObject readJSON(String filename) {
        JsonObject jsonObject = new JsonObject();
        try {
            JsonElement jsonElement = JsonParser.parseReader(new FileReader(filename));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {

        }
        return jsonObject;
    }

    @Before
    public void setUp() throws Exception {
        test1 = readJSON("./src/test/mockapi/rmptest01.json");
        test2 = readJSON("./src/test/mockapi/rmptest02.json");
        System.out.println(test1.toString());
    }

    @Test(timeout = 1000)
    public void testProfessorRatingSuccess() throws RateMyProfessorException {
        assert ProfessorRatingScraper.getRatingFromJSON(test1) == 5.0;
    }

    @Test(timeout = 1000)
    public void testProfessorRatingFailure() {
        assertThrows(
                RateMyProfessorException.class,
                () -> ProfessorRatingScraper.getRatingFromJSON(test2),
                "Professor not found");
    }
}