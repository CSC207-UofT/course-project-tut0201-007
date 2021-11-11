import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class SchedulerTest {
    Scheduler scheduleCreator = new Scheduler();
    CourseCreator courseCreator = new CourseCreator();
    ArrayList<String> codes = new ArrayList<>();
    Course course1;
    Course course2;

    @Before
    public void setUp() throws Exception {
        codes.add("TST101");
        codes.add("TST102");
        course1 = courseCreator.generateCourse(codes.get(0));
        course2 = courseCreator.generateCourse(codes.get(1));
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
                        + "LEC in ROOM 07 from 12:00 to 13:00 on MONDAY\n"
                        + "\nTutorials\n"
                        + "TUT in ROOM 05 from 18:00 to 21:00 on FRIDAY\n";
        // Checking that the course has the correct code
        assertEquals(expected, schedule.toString());
    }

    @Test(timeout = 1500)
    public void testGenerateMultiCourseSchedule() {
        Schedule schedule = scheduleCreator.createBasicSchedule(codes);

        String expected =
                "Schedule: \n"
                        + "\n"
                        + "Lectures\n"
                        + "LEC in NF 003 from 12:00 to 13:00 on MONDAY\n"
                        + "LEC in ROOM 07 from 12:00 to 13:00 on MONDAY\n"
                        + "\nTutorials\n"
                        + "TUT in ROOM 05 from 10:00 to 11:00 on TUESDAY\n"
                        + "TUT in ROOM 05 from 18:00 to 21:00 on FRIDAY\n";
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
                        + "LEC in ROOM 01 from 09:00 to 10:00 on WEDNESDAY\n"
                        + "\nTutorials\n";
        assertEquals(expected, schedule.toString());
    }
}
