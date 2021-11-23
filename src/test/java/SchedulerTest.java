import static org.junit.Assert.assertEquals;

import controllers.Controller;
import entities.Course;
import entities.Schedule;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import workers.CourseCreator;
import workers.Scheduler;

public class SchedulerTest {
    Scheduler scheduleCreator = new Scheduler();
    Course course1;
    Course course2;

    @Before
    public void setUp() throws Exception {
        course1 = CourseCreator.generateCourse("TST101", 'Y');
        course2 = CourseCreator.generateCourse("TST102", 'Y');
    }

    @Test(timeout = 1000)
    public void testGenerateTST105Schedule() {
        ArrayList<String> tst105 = new ArrayList<>();
        tst105.add("TST105F");
        ArrayList<Course> tst105C = (ArrayList<Course>) Controller.courseInstantiator(tst105);
        Schedule schedule = scheduleCreator.createBasicSchedule(tst105C);

        String expected =
                "Schedule: \n"
                        + "\n"
                        + "Lectures\n"
                        + "TST105 LEC-0101 F meets at:\n"
                        + "MONDAY from 10:00-11:00 at WI 1016 in session F\n"
                        + "TUESDAY from 18:00-21:00 at Contact DEPT in session F\n"
                        + "WEDNESDAY from 10:00-11:00 at WI 1016 in session F\n"
                        + "FRIDAY from 10:00-11:00 at WI 1016 in session F\n"
                        + "\n"
                        + "\n"
                        + "Tutorials\n";

        // Checking that the course has the correct code
        assertEquals(expected, schedule.toString());
    }

    @Test(timeout = 1500)
    public void testGenerateMultiCourseSchedule() {
        ArrayList<String> multi = new ArrayList<>();
        multi.add("TST107F");
        multi.add("TST105F");
        ArrayList<Course> multiC = (ArrayList<Course>) Controller.courseInstantiator(multi);
        Schedule schedule = scheduleCreator.createBasicSchedule(multiC);

        String expected =
                "Schedule: \n"
                        + "\n"
                        + "Lectures\n"
                        + "TST107 LEC-9901 F meets at:\n"
                        + "MONDAY from 09:00-10:00 at ONLINE in session F\n"
                        + "WEDNESDAY from 09:00-11:00 at ONLINE in session F\n"
                        + "\n"
                        + "TST105 LEC-0101 F meets at:\n"
                        + "MONDAY from 10:00-11:00 at WI 1016 in session F\n"
                        + "TUESDAY from 18:00-21:00 at Contact DEPT in session F\n"
                        + "WEDNESDAY from 10:00-11:00 at WI 1016 in session F\n"
                        + "FRIDAY from 10:00-11:00 at WI 1016 in session F\n"
                        + "\n"
                        + "\n"
                        + "Tutorials\n";

        assertEquals(expected, schedule.toString());
    }

    @Test(timeout = 1500)
    public void testGenerateNoTutorialSchedule() {
        ArrayList<String> tst104 = new ArrayList<>();
        tst104.add("TST105F");
        ArrayList<Course> tst104C = (ArrayList<Course>) Controller.courseInstantiator(tst104);
        Schedule schedule = scheduleCreator.createBasicSchedule(tst104C);
        String expected =
                "Schedule: \n"
                        + "\n"
                        + "Lectures\n"
                        + "TST105 LEC-0101 F meets at:\n"
                        + "MONDAY from 10:00-11:00 at WI 1016 in session F\n"
                        + "TUESDAY from 18:00-21:00 at Contact DEPT in session F\n"
                        + "WEDNESDAY from 10:00-11:00 at WI 1016 in session F\n"
                        + "FRIDAY from 10:00-11:00 at WI 1016 in session F\n"
                        + "\n"
                        + "\n"
                        + "Tutorials\n";

        assertEquals(expected, schedule.toString());
    }

}
