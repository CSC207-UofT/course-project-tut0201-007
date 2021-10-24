import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ScheduleExporterTest {

    ScheduleExporter exporter;
    Session inPersonSession, onlineSession;


    @Before
    public void setUp() {
        // TODO: if we mock api, get course for mocked course. If not, hardcode in a session
        try{
            inPersonSession = new CourseCreator().generateCourse("MAT237").getLectures().get(0);
            onlineSession = new CourseCreator().generateCourse("STA247").getLectures().get(0);
        }
        catch (IOException e){
            //idk what to do here? like it'll just fail all the tests, which i guess is correct
        }
    }

    @Test(timeout=100)
    public void testSessionExportInPerson(){
    }
    @Test(timeout=100)
    public void testSessionExportOnline(){

    }

}
