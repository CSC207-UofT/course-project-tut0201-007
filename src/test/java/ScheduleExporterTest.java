import entities.Schedule;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import workers.ScheduleExporter;
import workers.Scheduler;

public class ScheduleExporterTest {

    ScheduleExporter exporter;
    Schedule inPersonSchedule, onlineSchedule;

    @Before
    public void setUp() throws IOException {
        // TODO: if we mock api, get course for mocked course. If not, hardcode in a session
        ArrayList<String> inPerson = new ArrayList<>();
        ArrayList<String> online = new ArrayList<>();
        inPerson.add("MAT237");
        online.add("STA247");
        Scheduler scheduler = new Scheduler();
        inPersonSchedule = scheduler.createBasicSchedule(inPerson);
        onlineSchedule = scheduler.createBasicSchedule(online);

        exporter = new ScheduleExporter();
    }

    @Test(timeout = 100)
    public void testSessionExportInPerson() {
        // TODO: Fill this in later
    }

    @Test(timeout = 100)
    public void testSessionExportOnline() {
        // TODO: fill this in later.
    }
}
