import static org.junit.Assert.*;

import controllers.Controller;
import entities.Course;
import entities.Schedule;
import filters.CourseExclusionFilter;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import workers.Scheduler;


public class CourseExclusionFilterTest {
    CourseExclusionFilter filter;
    Scheduler scheduleCreator = new Scheduler();

    @Before
    public void setUp() {
        filter = new CourseExclusionFilter();
    }

    @Test(timeout = 1000)
    public void filterFail() {
        ArrayList<String> multi = new ArrayList<>();
        // Courses are not exclusions
        multi.add("TST101");
        multi.add("TST104");
        ArrayList<Course> courses = (ArrayList<Course>) Controller.courseInstantiator(multi);
        Schedule schedule = scheduleCreator.createBasicSchedule(courses);
        System.out.println(schedule.getCourses());
        assertFalse(filter.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void filterSucceed() {
        ArrayList<String> multi = new ArrayList<>();
        // Courses are exclusions
        multi.add("TST101");
        multi.add("TST102");
        ArrayList<Course> courses = (ArrayList<Course>) Controller.courseInstantiator(multi);
        Schedule schedule = scheduleCreator.createBasicSchedule(courses);
        assertTrue(filter.checkSchedule(schedule));
    }

}