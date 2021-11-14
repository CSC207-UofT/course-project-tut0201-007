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
    public void testGenerateTST101Schedule() {
        ArrayList<String> tst101 = new ArrayList<>();
        tst101.add("TST101");
        ArrayList<Course> tst101_c = (ArrayList<Course>) Controller.courseInstantiator(tst101);
        Schedule schedule = scheduleCreator.createBasicSchedule(tst101_c);

        String expected =
                "Schedule: \n"
                        + "\n"
                        + "Lectures\n"
                        + "TST101 LEC-0101 F meets at:\n"
                        + "MONDAY from 12:00-13:00 at ROOM 07 in session F\n"
                        + "\n"
                        + "\n"
                        + "Tutorials\n"
                        + "TST101 TUT-0101 F meets at:\n"
                        + "FRIDAY from 18:00-21:00 at ROOM 05 in session F\n\n";
        // Checking that the course has the correct code
        assertEquals(expected, schedule.toString());
    }

    @Test(timeout = 1500)
    public void testGenerateMultiCourseSchedule() {
        ArrayList<String> multi = new ArrayList<>();
        multi.add("TST101");
        multi.add("TST102");
        ArrayList<Course> mutli_c = (ArrayList<Course>) Controller.courseInstantiator(multi);
        Schedule schedule = scheduleCreator.createBasicSchedule(mutli_c);

        String expected =
                "Schedule: \n"
                        + "\n"
                        + "Lectures\n"
                        + "TST101 LEC-0101 F meets at:\n"
                        + "MONDAY from 12:00-13:00 at ROOM 07 in session F\n"
                        + "\n"
                        + "TST102 LEC-0101 F meets at:\n"
                        + "MONDAY from 09:00-10:00 at LM 159 in session F\n"
                        + "TUESDAY from 09:00-10:00 at LM 159 in session F\n"
                        + "THURSDAY from 09:00-10:00 at LM 159 in session F\n"
                        + "\n"
                        + "\n"
                        + "Tutorials\n"
                        + "TST101 TUT-0101 F meets at:\n"
                        + "FRIDAY from 18:00-21:00 at ROOM 05 in session F\n"
                        + "\n"
                        + "TST102 TUT-0101 F meets at:\n"
                        + "TUESDAY from 13:00-14:00 at WB 119 in session F\n\n";
        assertEquals(expected, schedule.toString());
    }

    @Test(timeout = 1500)
    public void testGenerateNoTutorialSchedule() {
        ArrayList<String> tst104 = new ArrayList<>();
        tst104.add("TST104");
        ArrayList<Course> tst104_c = (ArrayList<Course>) Controller.courseInstantiator(tst104);
        Schedule schedule = scheduleCreator.createBasicSchedule(tst104_c);
        String expected =
                "Schedule: \n"
                        + "\n"
                        + "Lectures\n"
                        + "TST104 LEC-0101 F meets at:\n"
                        + "WEDNESDAY from 09:00-10:00 at ROOM 01 in session F\n"
                        + "\n"
                        + "\n"
                        + "Tutorials\n";
        assertEquals(expected, schedule.toString());
    }
}
