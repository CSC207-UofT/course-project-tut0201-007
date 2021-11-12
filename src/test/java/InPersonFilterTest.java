import entities.Schedule;
import filters.InPersonFilter;
import filters.SpaceFilter;
import org.junit.Before;
import org.junit.Test;
import workers.Scheduler;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InPersonFilterTest {
    InPersonFilter filterInPerson;
    InPersonFilter filterOnline;
    Scheduler scheduleCreator;

    @Before
    public void setUp() {
        filterInPerson = new InPersonFilter(true);
        filterOnline = new InPersonFilter(false);
        scheduleCreator = new Scheduler();
    }

    @Test(timeout = 2000)
    public void filterInPersonSucceed() {
        ArrayList<String> tst102 = new ArrayList<>();
        tst102.add("TST102");
        Schedule schedule = scheduleCreator.createBasicSchedule(tst102);
        assertTrue(filterInPerson.checkSchedule(schedule));
    }

    @Test(timeout = 2000)
    public void filterInPersonFail() {
        ArrayList<String> tst103 = new ArrayList<>();
        tst103.add("TST103");
        Schedule schedule = scheduleCreator.createBasicSchedule(tst103);
        assertFalse(filterInPerson.checkSchedule(schedule));
    }

    @Test(timeout = 2000)
    public void filterOnlineSucceed() {
        ArrayList<String> tst103 = new ArrayList<>();
        tst103.add("TST103");
        Schedule schedule = scheduleCreator.createBasicSchedule(tst103);
        assertTrue(filterOnline.checkSchedule(schedule));
    }

    @Test(timeout = 2000)
    public void filterOnlineFail() {
        ArrayList<String> tst101 = new ArrayList<>();
        tst101.add("TST101");
        Schedule schedule = scheduleCreator.createBasicSchedule(tst101);
        assertFalse(filterOnline.checkSchedule(schedule));
    }
}
