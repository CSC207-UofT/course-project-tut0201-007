import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class APIWorkerTest {
    APIWorker getter;

    @Before
    public void setUp() throws Exception {
        getter = new APIWorker("MAT237");
    }

    @Test(timeout = 50)
    public void testJSONReturn() {
        String expected = "MAT237Y1";
        // Multiple casts because this package sucks
        assertEquals(expected, getter.info.get("MAT237Y1-Y-20219").getAsJsonObject().get("code").getAsString());
    }
}
