package workersTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import workers.APIWorker;

public class APIWorkerTest {
    APIWorker getter237;
    APIWorker getter244;

    @Before
    public void setUp() throws Exception {
        getter237 = new APIWorker("MAT237");
        getter244 = new APIWorker("MAT244");
    }

    @Test(timeout = 50)
    public void testJSONReturn() {
        String expected = "MAT237Y1";
        // Multiple casts because this package sucks
        // Checking that the code is what it should be in the Json object variable info
        assertEquals(
                expected,
                getter237.info.get("MAT237Y1-Y-20219").getAsJsonObject().get("code").getAsString());
    }

    @Test(timeout = 50)
    public void testStringRep() {
        String expected = "MAT237Y1-Y-20219";
        // Just checking that the semester key in the JSON is good
        assertEquals(expected, getter237.semester.get(0));
    }

    @Test(timeout = 50)
    public void testMultipleStringRep() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("MAT244H1-F-20219");
        expected.add("MAT244H1-S-20219");
        // Just checking that the semester keys in the JSON are good
        assertEquals(expected, getter244.semester);
    }
}
