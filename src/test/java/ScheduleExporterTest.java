import entities.Schedule;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import workers.ScheduleExporter;
import workers.ScheduleImporter;
import workers.Scheduler;

public class ScheduleExporterTest {

    Schedule coursesSchedule;

    @Before
    public void setUp() throws IOException {

        ArrayList<String> courses = new ArrayList<>();
        courses.add("TST101");
        Scheduler scheduler = new Scheduler();
        coursesSchedule = scheduler.createBasicSchedule(courses);
    }

    @Test(timeout = 1000)
    public void testInvertability() {
        StringWriter writer = new StringWriter();
        ScheduleExporter.outputScheduleICS(coursesSchedule, writer);
        Schedule output = ScheduleImporter.importSchedule(new StringReader(writer.toString()));
        assert (output.equals(coursesSchedule));
    }
}
