import controllers.Controller;
import entities.Course;
import entities.Schedule;
import java.io.StringReader;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import workers.ScheduleImporter;
import workers.Scheduler;

public class ScheduleImporterTest {

    String dummyICS;

    @Before
    public void setUp() {
        dummyICS ="BEGIN:VCALENDAR\n" +
                "PRODID:-//CSC207 Team 007//iCal4j 1.0//EN\n" +
                "VERSION:2.0\n" +
                "CALSCALE:GREGORIAN\n" +
                "BEGIN:VEVENT\n" +
                "DTSTAMP:20211112T222307Z\n" +
                "DTSTART:20210910T180000\n" +
                "DTEND:20210910T210000\n" +
                "SUMMARY:TST101 TUT-0101 F\n" +
                "RRULE:FREQ=WEEKLY;UNTIL=20211210\n" +
                "UID:8e14863e-5731-4333-ab83-1f92eabdcad4\n" +
                "LOCATION:ROOM 05\n" +
                "END:VEVENT\n" +
                "BEGIN:VEVENT\n" +
                "DTSTAMP:20211112T222307Z\n" +
                "DTSTART:20210913T120000\n" +
                "DTEND:20210913T130000\n" +
                "SUMMARY:TST101 LEC-0101 F\n" +
                "RRULE:FREQ=WEEKLY;UNTIL=20211210\n" +
                "UID:005e6401-a512-465d-9767-3d4397ec0dd1\n" +
                "LOCATION:ROOM 07\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";

    }

    @Test(timeout = 1000)
    public void testImport() {
        StringReader stringReader = new StringReader(dummyICS);
        Schedule res = ScheduleImporter.importSchedule(stringReader);

        Scheduler s = new Scheduler();
        ArrayList<String> courseCodes = new ArrayList<>();
        courseCodes.add("TST101");
        ArrayList<Course> courses = (ArrayList<Course>) Controller.courseInstantiator(courseCodes);
        Schedule expected = s.createBasicSchedule(courses);

        assert (res.equals(expected));
    }
}
