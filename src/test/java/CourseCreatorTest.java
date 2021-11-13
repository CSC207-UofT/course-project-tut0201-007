import static org.junit.Assert.*;

import entities.Course;
import org.junit.Before;
import org.junit.Test;
import workers.CourseCreator;

import java.util.ArrayList;
import java.util.List;

public class CourseCreatorTest {
    Course mockCourse1;
    Course mockCourse2;

    @Before
    public void setUp() throws Exception {
        mockCourse1 = CourseCreator.generateCourse("TST101", 'Y');
        mockCourse2 = CourseCreator.generateCourse("TST104", 'Y');
    }

    @Test(timeout = 1000)
    public void testGenerateMockCourse() {
        String expected = "TST101";
        // Checking that the mock course has the correct code
        assertEquals(expected, mockCourse1.getCourseId());
    }

    @Test(timeout = 1000)
    public void testMockLectureSessions() {
        String expected = "TST101 LEC-0101 Y meets at:\n" + "MONDAY from 12:00-13:00 at ROOM 07\n";
        // Checking that the course has the correct lecture
        assertEquals(expected, mockCourse1.getLectures().get(0).toString());
    }

    @Test(timeout = 1000)
    public void testMockTutorialSessions() {
        String expected = "TST101 TUT-0101 Y meets at:\nFRIDAY from 18:00-21:00 at ROOM 05\n";
        // Checking that the course has the correct tutorial
        assertEquals(expected, mockCourse1.getTutorials().get(0).toString());
    }

    @Test(timeout = 1000)
    public void testMockExclusion1() {
        ArrayList<String> expected = new ArrayList<>(List.of("TST102"));
        // Checking that the course has the correct exclusions
        assertEquals(expected, mockCourse1.getExclusions());
    }

    @Test(timeout = 1000)
    public void testMockExclusion2() {
        ArrayList<String> expected = new ArrayList<>(List.of("TST100", "TST101", "TST105"));
        // Checking that the course has the correct exclusions
        assertEquals(expected, mockCourse2.getExclusions());
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
