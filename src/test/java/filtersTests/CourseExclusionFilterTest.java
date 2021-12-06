package filtersTests;

import static org.junit.Assert.*;

import controllers.Controller;
import entities.Course;
import entities.Schedule;
import filters.CourseExclusionFilter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import workers.Scheduler;

public class CourseExclusionFilterTest {
    CourseExclusionFilter filter1;
    CourseExclusionFilter filter2;
    Scheduler scheduleCreator;
    List<Course> courses;

    @Before
    public void setUp() {
        scheduleCreator = new Scheduler();
        courses = new ArrayList<>();
    }

    @Test(timeout = 1000)
    public void filterFail() {
        List<String> multi = new ArrayList<>();
        // Courses are not exclusions
        multi.add("TST101Y");
        multi.add("TST104Y");
        List<Course> courses = Controller.courseInstantiator(multi);
        filter1 = new CourseExclusionFilter(courses);
        Schedule schedule = scheduleCreator.createBasicSchedule(courses);
        assertFalse(filter1.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void filterSucceed() {
        List<String> multi = new ArrayList<>();
        // Courses are exclusions
        multi.add("TST101Y");
        multi.add("TST102Y");
        List<Course> courses = Controller.courseInstantiator(multi);
        filter2 = new CourseExclusionFilter(courses);
        Schedule schedule = scheduleCreator.createBasicSchedule(courses);
        assertTrue(filter2.checkSchedule(schedule));
    }
}
