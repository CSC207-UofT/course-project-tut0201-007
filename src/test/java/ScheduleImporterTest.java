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
        dummyICS =
                "BEGIN:VCALENDAR\n"
                        + "PRODID:-//CSC207 Team 007//iCal4j 1.0//EN\n"
                        + "VERSION:2.0\n"
                        + "CALSCALE:GREGORIAN\n"
                        + "BEGIN:VEVENT\n"
                        + "DTSTAMP:20211112T183502Z\n"
                        + "DTSTART:20210914T100000\n"
                        + "DTEND:20210914T110000\n"
                        + "SUMMARY:MAT237 TUT-0101 F\n"
                        + "RRULE:FREQ=WEEKLY;UNTIL=20211210\n"
                        + "UID:205ae425-a5e5-44a9-a51a-1ca4c3a0f8ba\n"
                        + "LOCATION:MY 380\n"
                        + "END:VEVENT\n"
                        + "BEGIN:VEVENT\n"
                        + "DTSTAMP:20211112T183502Z\n"
                        + "DTSTART:20210913T090000\n"
                        + "DTEND:20210913T100000\n"
                        + "SUMMARY:MAT237 LEC-0101 F\n"
                        + "RRULE:FREQ=WEEKLY;UNTIL=20211210\n"
                        + "UID:f52089d7-d35a-4207-90ef-9934ebd8aeb8\n"
                        + "LOCATION:MP 203\n"
                        + "END:VEVENT\n"
                        + "BEGIN:VEVENT\n"
                        + "DTSTAMP:20211112T183502Z\n"
                        + "DTSTART:20210914T090000\n"
                        + "DTEND:20210914T100000\n"
                        + "SUMMARY:MAT237 LEC-0101 F\n"
                        + "RRULE:FREQ=WEEKLY;UNTIL=20211210\n"
                        + "UID:d3e97506-56e8-48f7-9342-400b8d770d73\n"
                        + "LOCATION:MP 103\n"
                        + "END:VEVENT\n"
                        + "BEGIN:VEVENT\n"
                        + "DTSTAMP:20211112T183502Z\n"
                        + "DTSTART:20210909T090000\n"
                        + "DTEND:20210909T100000\n"
                        + "SUMMARY:MAT237 LEC-0101 F\n"
                        + "RRULE:FREQ=WEEKLY;UNTIL=20211210\n"
                        + "UID:df91ef8d-da98-4649-b884-16fbb7b96e75\n"
                        + "LOCATION:MP 203\n"
                        + "END:VEVENT\n"
                        + "END:VCALENDAR";
    }

    @Test(timeout = 1000)
    public void testImport() {
        StringReader stringReader = new StringReader(dummyICS);
        Schedule res = ScheduleImporter.importSchedule(stringReader);

        Scheduler s = new Scheduler();
        ArrayList<String> courses = new ArrayList<>();
        courses.add("MAT237");
        Schedule expected = s.createBasicSchedule(courses);

        assert (res.equals(expected));
    }
}
