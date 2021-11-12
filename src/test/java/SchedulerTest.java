import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

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
        Schedule schedule = scheduleCreator.createBasicSchedule(tst101);

        String expected =
                "Schedule: \n"
                        + "\n"
                        + "Lectures\n"
                        + "LEC-0101 meets at:\n"
                        + "MONDAY from 12:00-13:00 at ROOM 07\n"
                        + "\n\nTutorials\n"
                        + "TUT-0101 meets at:\n"
                        + "FRIDAY from 18:00-21:00 at ROOM 05\n\n";
        // Checking that the course has the correct code
        assertEquals(expected, schedule.toString());
    }

    @Test(timeout = 1500)
    public void testGenerateMultiCourseSchedule() {
        ArrayList<String> multi = new ArrayList<>();
        multi.add("TST101");
        multi.add("TST102");
        Schedule schedule = scheduleCreator.createBasicSchedule(multi);

        String expected =
                "Schedule: \n"
                        + "\n"
                        + "Lectures\n"
                        + "LEC-0101 meets at:\n"
                        + "MONDAY from 12:00-13:00 at ROOM 07\n"
                        + "\n"
                        + "LEC-0101 meets at:\n"
                        + "MONDAY from 12:00-13:00 at NF 003\n"
                        + "TUESDAY from 12:00-13:00 at NF 003\n"
                        + "FRIDAY from 13:00-14:00 at NF 003\n"
                        + "\n\nTutorials\n"
                        + "TUT-0101 meets at:\n"
                        + "FRIDAY from 18:00-21:00 at ROOM 05\n"
                        + "\n"
                        + "TUT-0101 meets at:\n"
                        + "TUESDAY from 10:00-11:00 at ROOM 05\n\n";
        assertEquals(expected, schedule.toString());
    }

    @Test(timeout = 1500)
    public void testGenerateNoTutorialSchedule() {
        ArrayList<String> tst104 = new ArrayList<>();
        tst104.add("TST104");
        Schedule schedule = scheduleCreator.createBasicSchedule(tst104);
        String expected =
                "Schedule: \n"
                        + "\n"
                        + "Lectures\n"
                        + "LEC-0101 meets at:\n"
                        + "WEDNESDAY from 09:00-10:00 at ROOM 01\n"
                        + "\n"
                        + "\n"
                        + "Tutorials\n";
        assertEquals(expected, schedule.toString());
    }
}
