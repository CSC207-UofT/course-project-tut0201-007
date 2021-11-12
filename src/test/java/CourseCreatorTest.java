import static org.junit.Assert.*;

import entities.Course;
import org.junit.Before;
import org.junit.Test;
import workers.CourseCreator;

import java.util.ArrayList;
import java.util.List;

public class CourseCreatorTest {
    Course mockCourse;

    @Before
    public void setUp() throws Exception {
        mockCourse = CourseCreator.generateCourse("TST101", 'Y');
    }

    @Test(timeout = 1000)
    public void testGenerateMockCourse() {
        String expected = "TST101";
        // Checking that the mock course has the correct code
        assertEquals(expected, mockCourse.getCourseId());
    }

    @Test(timeout = 1000)
    public void testMockLectureSessions() {
        String expected = "LEC-0101 meets at:\n" + "MONDAY from 12:00-13:00 at ROOM 07\n";
        // Checking that the course has the correct lecture
        assertEquals(expected, mockCourse.getLectures().get(0).toString());
    }

    @Test(timeout = 1000)
    public void testMockTutorialSessions() {
        String expected = "TUT-0101 meets at:\nFRIDAY from 18:00-21:00 at ROOM 05\n";
        // Checking that the course has the correct tutorial
        assertEquals(expected, mockCourse.getTutorials().get(0).toString());
    }

    @Test(timeout = 1000)
    public void testMockExclusion() {
        ArrayList<String> expected = new ArrayList<>(List.of("MAT137Y1", "MATA37H3", "MAT137Y5", "MAT157Y5", "MAT197H1", "ESC195H1"));
        // Checking that the course has the correct exclusions
        assertEquals(expected, mockCourse.getExclusions());
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
