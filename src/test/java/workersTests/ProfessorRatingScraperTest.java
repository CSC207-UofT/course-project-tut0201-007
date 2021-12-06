package workersTests;

import org.junit.Before;
import org.junit.Test;

import util.RateMyProfessorException;

import java.io.IOException;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import workers.ProfessorRatingScraper;

public class ProfessorRatingScraperTest {
    String html1, html2;

    @Before
    public void setUp() throws Exception {
        html1 = parseHTMLFile("./src/test/mockhtml/test1.html");
        html2 = parseHTMLFile("./src.test/mockhtml/test2.html");
    }

    private String parseHTMLFile(String filepath) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(filepath));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
        }
        return contentBuilder.toString();
    }

    @Test(timeout = 2000)
    public void testGetRatingSuccess() throws RateMyProfessorException {
        assert ProfessorRatingScraper.getRatingFromHTML(html1) == 4.1;
    }

    @Test(timeout = 2000)
    public void testGetRatingFailure() {
        assertThrows(RateMyProfessorException.class, () -> ProfessorRatingScraper.getRatingFromHTML(html2), "Professor not found");
    }
}
