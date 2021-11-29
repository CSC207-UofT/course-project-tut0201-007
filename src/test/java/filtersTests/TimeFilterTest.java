package filtersTests;

import static org.junit.Assert.assertFalse;

import controllers.Controller;
import entities.Course;
import entities.Schedule;
import filters.TimeFilter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import workers.Scheduler;

public class TimeFilterTest {
    Schedule schedule;

    @Before
    public void setUp() {
        List<String> courseIDs = new ArrayList<>();
        courseIDs.add("TST101Y");
        courseIDs.add("TST102Y");
        List<Course> courses = Controller.courseInstantiator(courseIDs);

        Scheduler scheduleCreator = new Scheduler();
        schedule = scheduleCreator.createBasicSchedule(courses);
    }

    @Test(timeout = 1000)
    public void mondayFilterAccept() {
        LocalTime start = LocalTime.of(12, 0);
        LocalTime end = LocalTime.of(16, 0);
        TimeFilter mondayFilter = new TimeFilter(start, end, TimeFilter.Day.MONDAY);
        assert (mondayFilter.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void mondayFilterReject() {

        LocalTime start = LocalTime.of(16, 0);
        LocalTime end = LocalTime.of(20, 0);
        TimeFilter mondayFilter = new TimeFilter(start, end, TimeFilter.Day.MONDAY);
        assertFalse(mondayFilter.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void generalFilterAccept() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(21, 0);
        TimeFilter generalFilter = new TimeFilter(start, end, TimeFilter.Day.ALL_DAYS);
        assert (generalFilter.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void generalFilterReject() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 0);
        TimeFilter generalFilter = new TimeFilter(start, end, TimeFilter.Day.ALL_DAYS);
        assertFalse(generalFilter.checkSchedule(schedule));
    }
}
