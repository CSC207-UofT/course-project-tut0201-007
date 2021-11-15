import static org.junit.Assert.assertFalse;

import controllers.Controller;
import entities.Course;
import entities.Schedule;
import filters.ConflictFilter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import workers.Scheduler;

public class ConflictFilterTest {
    ConflictFilter conflictFilter;
    Scheduler scheduler;

    @Before
    public void setUp() throws Exception {
        conflictFilter = new ConflictFilter();
        scheduler = new Scheduler();
    }

    @Test(timeout = 1000)
    public void testFilterAccepts() {
        List<String> courseIDs = new ArrayList<>();
        courseIDs.add("TST101Y");
        courseIDs.add("TST104Y");
        ArrayList<Course> courses = (ArrayList<Course>) Controller.courseInstantiator(courseIDs);

        Schedule schedule = scheduler.createBasicSchedule(courses);
        assert (conflictFilter.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void testFilterRejects() {
        List<String> courseIDs = new ArrayList<>();
        courseIDs.add("TST101Y");
        courseIDs.add("TST102Y");
        ArrayList<Course> courses = (ArrayList<Course>) Controller.courseInstantiator(courseIDs);

        Schedule schedule = scheduler.createBasicSchedule(courses);

        assertFalse(conflictFilter.checkSchedule(schedule));
    }

    @Test(timeout = 1000)
    public void testFilterRejects2() {
        List<String> courseIDs = new ArrayList<>();
        courseIDs.add("TST106Y");
        courseIDs.add("TST107Y");

        ArrayList<Course> courses = (ArrayList<Course>) Controller.courseInstantiator(courseIDs);

        Schedule schedule = scheduler.createBasicSchedule(courses);


        assertFalse(conflictFilter.checkSchedule(schedule));
    }
}
