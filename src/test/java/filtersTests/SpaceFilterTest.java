package filtersTests;

import static org.junit.Assert.*;

import controllers.Controller;
import entities.Course;
import entities.Schedule;
import filters.SpaceFilter;
import java.util.ArrayList;
import java.util.List;
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
        multi.add("TST101Y");
        multi.add("TST102Y");
        List<Course> courses = Controller.courseInstantiator(multi);
        Schedule schedule = scheduleCreator.createBasicSchedule(courses);
        assertFalse(filter.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void filterSucceed() {
        ArrayList<String> multi = new ArrayList<>();
        multi.add("TST101Y");
        multi.add("TST104Y");
        List<Course> courses = Controller.courseInstantiator(multi);
        Schedule schedule = scheduleCreator.createBasicSchedule(courses);
        assertTrue(filter.checkSchedule(schedule));
    }
}
