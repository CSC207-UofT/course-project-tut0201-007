package workersTests;

import static org.junit.Assert.*;

import entities.Course;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import workers.CourseCreator;

public class CourseCreatorTest {
    Course mockCourse1, mockCourse2, sCourse, fCourse;

    @Before
    public void setUp() throws Exception {
        mockCourse1 = CourseCreator.generateCourse("TST101", 'Y');
        mockCourse2 = CourseCreator.generateCourse("TST104", 'Y');
        fCourse = CourseCreator.generateCourse("TST105", 'F');
        sCourse = CourseCreator.generateCourse("TST105", 'S');
    }

    @Test(timeout = 1000)
    public void testGenerateMockCourse() {
        String expected = "TST101";
        // Checking that the mock course has the correct code
        assertEquals(expected, mockCourse1.getCourseId());
    }

    @Test(timeout = 1000)
    public void testMockLectureSessions() {
        String expected =
                "TST101 LEC-0101 Y meets at:\n"
                        + "MONDAY from 12:00-13:00 at ROOM 07 in session F\n"
                        + "MONDAY from 12:00-13:00 at ROOM 08 in session S\n";
        // Checking that the course has the correct lecture
        assertEquals(expected, mockCourse1.getLectures().get(0).toString());
    }

    @Test(timeout = 1000)
    public void testMockTutorialSessions() {
        String expected =
                "TST101 TUT-0101 Y meets at:\n"
                        + "FRIDAY from 18:00-21:00 at ROOM 05 in session F\n"
                        + "FRIDAY from 18:00-21:00 at ROOM 06 in session S\n";
        // Checking that the course has the correct tutorial
        assertEquals(expected, mockCourse1.getTutorials().get(0).toString());
    }

    @Test(timeout = 1000)
    public void testMockExclusion1() {
        List<String> expected = List.of("TST102");
        // Checking that the course has the correct exclusions
        assertEquals(expected, mockCourse1.getExclusions());
    }

    @Test(timeout = 1000)
    public void testMockExclusion2() {
        List<String> expected = List.of("TST100", "TST106", "TST105");
        // Checking that the course has the correct exclusions
        assertEquals(expected, mockCourse2.getExclusions());
    }

    @Test(timeout = 1000)
    public void testMockCoreq1() {
        List<String> expected = List.of("TST103");
        // Checking that the course has the correct corequisites
        assertEquals(expected, mockCourse1.getCorequisites());
    }

    @Test(timeout = 1000)
    public void testMockCoreq2() {
        List<String> expected = List.of("TST107", "TST105");
        // Checking that the course has the correct corequisites
        assertEquals(expected, mockCourse2.getCorequisites());
    }

    @Test(timeout = 1000)
    public void testGenerateFCourse() {
        String expected = "TST105";
        assertEquals(expected, fCourse.getCourseId());
    }

    @Test(timeout = 1000)
    public void testGenerateSCourse() {
        String expected = "TST105";
        assertEquals(expected, sCourse.getCourseId());
    }
}
