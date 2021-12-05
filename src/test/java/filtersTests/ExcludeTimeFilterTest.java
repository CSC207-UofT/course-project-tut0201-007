package filtersTests;

import static org.junit.Assert.assertFalse;
import controllers.Controller;
import entities.Course;
import entities.Schedule;
import filters.Day;
import filters.ExcludeTimeFilter;

import filters.TimeFilter;
import org.junit.Before;
import org.junit.Test;
import workers.Scheduler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class ExcludeTimeFilterTest {
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
    public void mondayFilterReject() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(13, 0);
        ExcludeTimeFilter mondayFilter = new ExcludeTimeFilter(start, end, Day.MONDAY);
        assertFalse(mondayFilter.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void mondayFilterAccept() {
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(11, 0);
        ExcludeTimeFilter mondayFilter = new ExcludeTimeFilter(start, end, Day.MONDAY);
        assert(mondayFilter.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void generalFilterReject() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(21, 0);
        ExcludeTimeFilter generalFilter = new ExcludeTimeFilter(start, end, Day.ALL_DAYS);
        assertFalse(generalFilter.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void generalFilterAccept() {
        LocalTime start = LocalTime.of(11, 0);
        LocalTime end = LocalTime.of(12, 0);
        ExcludeTimeFilter generalFilter = new ExcludeTimeFilter(start, end, Day.ALL_DAYS);
        assert(generalFilter.checkSchedule(schedule));
    }
}
