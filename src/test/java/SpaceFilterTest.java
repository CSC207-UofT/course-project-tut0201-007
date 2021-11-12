import static org.junit.Assert.*;

import entities.Schedule;
import filters.SpaceFilter;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import workers.Scheduler;

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
        assertFalse(filter.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void filterSucceed() {
        ArrayList<String> multi = new ArrayList<>();
        multi.add("TST101");
        multi.add("TST104");
        Schedule schedule = scheduleCreator.createBasicSchedule(multi);
        assertTrue(filter.checkSchedule(schedule));
    }
}
