package filtersTests;

import filters.Day;
import org.junit.Before;
import org.junit.Test;

public class DayTest {
    Day day;

    @Before
    public void setUp() throws Exception {
        day = Day.MONDAY;
    }

    @Test(timeout = 10)
    public void testString() {
        assert (day.toString().equals("Monday"));
    }
}
