import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class SpaceFilterTest {
    SpaceFilter filter;
    Scheduler scheduleCreator = new Scheduler();

    @Before
    public void setUp() {
        filter = new SpaceFilter(1);
    }

    @Test(timeout = 1000)
    public void filterFail() {
        ArrayList<String> multi = new ArrayList<>();
        multi.add("TST101");
        multi.add("TST102");
        Schedule schedule = scheduleCreator.createBasicSchedule(multi);
        assertNull(filter.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void filterSucceed() {
        ArrayList<String> multi = new ArrayList<>();
        multi.add("TST101");
        multi.add("TST104");
        Schedule schedule = scheduleCreator.createBasicSchedule(multi);
        assertEquals(filter.checkSchedule(schedule), schedule);
    }
}
