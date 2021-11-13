import controllers.Controller;
import entities.Course;
import entities.Schedule;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import workers.ScheduleExporter;
import workers.ScheduleImporter;
import workers.Scheduler;

public class ScheduleExporterTest {

    Schedule coursesSchedule;

    @Before
    public void setUp() {

        ArrayList<String> courseCodes = new ArrayList<>();
        courseCodes.add("TST101");
        Scheduler scheduler = new Scheduler();
        List<Course> courses = Controller.courseInstantiator(courseCodes);
        coursesSchedule = scheduler.createBasicSchedule((ArrayList<Course>) courses);
    }

    @Test(timeout = 2000)
    public void testInvertability() {
        StringWriter writer = new StringWriter();
        ScheduleExporter.outputScheduleICS(coursesSchedule, writer);
        String writtenICS = writer.toString();
        Schedule output = ScheduleImporter.importSchedule(new StringReader(writtenICS));
        assert (output.equals(coursesSchedule));
    }
}
