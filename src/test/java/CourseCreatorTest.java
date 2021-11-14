import static org.junit.Assert.*;

import entities.Course;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import workers.CourseCreator;

public class CourseCreatorTest {
    Course mockCourse, sCourse, fCourse;

    @Before
    public void setUp() throws Exception {
        mockCourse = CourseCreator.generateCourse("TST101", 'Y');
        fCourse = CourseCreator.generateCourse("TST105", 'F');
        sCourse = CourseCreator.generateCourse("TST105", 'S');
    }

    @Test(timeout = 1000)
    public void testGenerateMockCourse() {
        String expected = "TST101";
        // Checking that the mock course has the correct code
        assertEquals(expected, mockCourse.getCourseId());
    }

    @Test(timeout = 1000)
    public void testMockLectureSessions() {
        String expected =
                "TST101 LEC-0101 Y meets at:\n"
                        + "MONDAY from 12:00-13:00 at ROOM 07 in session F\n"
                        + "MONDAY from 12:00-13:00 at ROOM 08 in session Y\n";
        // Checking that the course has the correct lecture
        assertEquals(expected, mockCourse.getLectures().get(0).toString());
    }

    @Test(timeout = 1000)
    public void testMockTutorialSessions() {
        String expected =
                "TST101 TUT-0101 Y meets at:\n"
                        + "FRIDAY from 18:00-21:00 at ROOM 05 in session F\n"
                        + "FRIDAY from 18:00-21:00 at ROOM 06 in session Y\n";
        // Checking that the course has the correct tutorial
        assertEquals(expected, mockCourse.getTutorials().get(0).toString());
    }

    @Test(timeout = 1000)
    public void testGenerateFCourse() throws IOException {
        String expected = "TST105";
        assertEquals(expected, fCourse.getCourseId());
    }

    @Test(timeout = 1000)
    public void testGenerateSCourse() throws IOException {
        String expected = "TST105";
        assertEquals(expected, sCourse.getCourseId());
    }

    //    @Test(timeout = 1000)
    //    public void testMockTutorialSessionsSync() {
    //        String expected = "TUT-0201 meets at:\nTUESDAY from 10:00-11:00 at ONLINE\n";
    //        // Checking that the course has the correct online tutorial
    //        assertEquals(expected, mockCourse.getTutorials().get(1).toString());
    //    }

    //    @Test(timeout = 1000)
    //    public void testMockTutorialSectionsAsync() {
    //        String expected = "TUT-0301 is ASYNC";
    //        // Checking that the course has no scheduled lectures
    //        assertEquals(expected, mockCourse.getLectures().get(2).toString());
    //    }
}
