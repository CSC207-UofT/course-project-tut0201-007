import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScheduleCreatorTest {
    ScheduleCreator scheduleCreator = new ScheduleCreator();
    CourseCreator courseCreator = new CourseCreator();
    ArrayList<String> codes = new ArrayList<>();
    Course course1;
    Course course2;

    @Before
    public void setUp() throws Exception {
        codes.add("MAT237");
        codes.add("MAT244");
        course1 = courseCreator.generateCourse(codes.get(0));
        course2 = courseCreator.generateCourse(codes.get(1));
    }

    @Test(timeout = 1000)
    public void testGenerateMAT237Schedule() {
        ArrayList<String> mat237 = new ArrayList<>();
        mat237.add("MAT237");
        Schedule schedule = scheduleCreator.createBasicSchedule(mat237);

        String expected = "Schedule: \n" +
                "\n" +
                "Lectures\n" +
                "LEC in MP 203 from 09:00 to 10:00 on MONDAY\n" +
                "\nTutorials\n" +
                "TUT in MY 380 from 10:00 to 11:00 on TUESDAY\n";
        // Checking that the course has the correct code
        assertEquals(expected, schedule.toString());
    }

    @Test(timeout = 1000)
    public void testGenerateMultiCourseSchedule() {
        Schedule schedule = scheduleCreator.createBasicSchedule(codes);


        String expected = "Schedule: \n" +
                "\n" +
                "Lectures\n" +
                "LEC in BA 1160 from 10:00 to 11:00 on MONDAY\n" +
                "LEC in MP 203 from 09:00 to 10:00 on MONDAY\n" +
                "\nTutorials\n" +
                "TUT in SS 1069 from 09:00 to 10:00 on MONDAY\n" +
                "TUT in MY 380 from 10:00 to 11:00 on TUESDAY\n";
        assertEquals(expected, schedule.toString());
    }

}
