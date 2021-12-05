package filtersTests;

import static org.junit.Assert.*;

import controllers.Controller;
import entities.Course;
import entities.Schedule;
import filters.CourseCoreqFilter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import workers.Scheduler;

public class CourseCoreqFilterTest {
    CourseCoreqFilter filter1;
    CourseCoreqFilter filter2;
    Scheduler scheduleCreator;
    ArrayList<Course> courses;

    @Before
    public void setUp() {
        scheduleCreator = new Scheduler();
        courses = new ArrayList<>();
    }

    @Test(timeout = 1000)
    public void filterFail() {
        ArrayList<String> multi = new ArrayList<>();
        // Courses are not coreqs
        multi.add("TST101Y");
        multi.add("TST104Y");
        List<Course> courses = Controller.courseInstantiator(multi);
        filter1 = new CourseCoreqFilter(courses);
        Schedule schedule = scheduleCreator.createBasicSchedule(courses);
        assertFalse(filter1.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void filterSucceed() {
        ArrayList<String> multi = new ArrayList<>();
        // Courses are coreqs
        multi.add("TST101Y");
        multi.add("TST103Y");
        List<Course> courses = Controller.courseInstantiator(multi);
        filter2 = new CourseCoreqFilter(courses);
        Schedule schedule = scheduleCreator.createBasicSchedule(courses);
        assertTrue(filter2.checkSchedule(schedule));
    }
}
