package workersTests;

import static org.junit.Assert.assertEquals;

import controllers.Controller;
import entities.Course;
import entities.Schedule;
import java.util.ArrayList;
import java.util.List;
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
        List<String> tst105 = new ArrayList<>();
        tst105.add("TST105F");
        List<Course> tst105C = Controller.courseInstantiator(tst105);
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
        List<String> multi = new ArrayList<>();
        multi.add("TST107F");
        multi.add("TST105F");
        List<Course> multiC = Controller.courseInstantiator(multi);
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
        List<String> tst104 = new ArrayList<>();
        tst104.add("TST105F");
        List<Course> tst104C = Controller.courseInstantiator(tst104);
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

    @Test(timeout = 1000)
    public void testGenerateYearCourseSchedule() {
        ArrayList<String> courseIDs = new ArrayList<>();
        courseIDs.add("TST102Y");

        ArrayList<Course> courses = (ArrayList<Course>) Controller.courseInstantiator(courseIDs);
        Schedule schedule = scheduleCreator.createBasicSchedule(courses);
        String expected =
                "Schedule: \n"
                        + "\n"
                        + "Lectures\n"
                        + "TST102 LEC-0101 Y meets at:\n"
                        + "MONDAY from 09:00-10:00 at LM 159 in session F\n"
                        + "MONDAY from 09:00-10:00 at LM 159 in session S\n"
                        + "TUESDAY from 09:00-10:00 at LM 159 in session F\n"
                        + "TUESDAY from 09:00-10:00 at LM 159 in session S\n"
                        + "THURSDAY from 09:00-10:00 at LM 159 in session F\n"
                        + "THURSDAY from 09:00-10:00 at LM 159 in session S\n"
                        + "\n"
                        + "\n"
                        + "Tutorials\n"
                        + "TST102 TUT-0101 Y meets at:\n"
                        + "TUESDAY from 13:00-14:00 at WB 119 in session F\n"
                        + "TUESDAY from 13:00-14:00 at WB 119 in session S\n"
                        + "\n";
        assertEquals(expected, schedule.toString());
    }
}
