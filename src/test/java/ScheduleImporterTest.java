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
        dummyICS =
                "BEGIN:VCALENDAR\n"
                        + "PRODID:-//CSC207 Team 007//iCal4j 1.0//EN\n"
                        + "VERSION:2.0\n"
                        + "CALSCALE:GREGORIAN\n"
                        + "BEGIN:VEVENT\n"
                        + "DTSTAMP:20211112T222803Z\n"
                        + "DTSTART:20210910T180000\n"
                        + "DTEND:20210910T210000\n"
                        + "SUMMARY:TST101 TUT-0101 F\n"
                        + "RRULE:FREQ=WEEKLY;UNTIL=20211210\n"
                        + "UID:ed9cb5e6-c588-41e6-810e-e4d98b072b53\n"
                        + "LOCATION:ROOM 05\n"
                        + "END:VEVENT\n"
                        + "BEGIN:VEVENT\n"
                        + "DTSTAMP:20211112T222803Z\n"
                        + "DTSTART:20210913T120000\n"
                        + "DTEND:20210913T130000\n"
                        + "SUMMARY:TST101 LEC-0101 F\n"
                        + "RRULE:FREQ=WEEKLY;UNTIL=20211210\n"
                        + "UID:4fc6f442-67ba-48dc-8c34-2c30709a1d57\n"
                        + "LOCATION:ROOM 07\n"
                        + "END:VEVENT\n"
                        + "END:VCALENDAR";
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

    @Test(timeout = 1000)
    public void testYSessionImport(){
        //TODO: Need to be able to pass in course codes with S/Y/F included
        String ySessionICS= "BEGIN:VCALENDAR\n" +
                "PRODID:-//CSC207 Team 007//iCal4j 1.0//EN\n" +
                "VERSION:2.0\n" +
                "CALSCALE:GREGORIAN\n" +
                "BEGIN:VEVENT\n" +
                "DTSTAMP:20211115T013823Z\n" +
                "DTSTART:20210910T180000\n" +
                "DTEND:20210910T210000\n" +
                "SUMMARY:TST101 TUT-0101 Y\n" +
                "RRULE:FREQ=WEEKLY;UNTIL=20211210\n" +
                "UID:78545386-ef55-4a1e-b26c-7349f215c041\n" +
                "LOCATION:ROOM 05\n" +
                "END:VEVENT\n" +
                "BEGIN:VEVENT\n" +
                "DTSTAMP:20211115T013823Z\n" +
                "DTSTART:20220114T180000\n" +
                "DTEND:20220114T210000\n" +
                "SUMMARY:TST101 TUT-0101 Y\n" +
                "RRULE:FREQ=WEEKLY;UNTIL=20220411\n" +
                "UID:b34dff12-3622-4240-a2a4-f68cda238fa4\n" +
                "LOCATION:ROOM 06\n" +
                "END:VEVENT\n" +
                "BEGIN:VEVENT\n" +
                "DTSTAMP:20211115T013823Z\n" +
                "DTSTART:20210913T120000\n" +
                "DTEND:20210913T130000\n" +
                "SUMMARY:TST101 LEC-0101 Y\n" +
                "RRULE:FREQ=WEEKLY;UNTIL=20211210\n" +
                "UID:0930fd3b-79f9-43f2-a4a0-f8d7b67aa5be\n" +
                "LOCATION:ROOM 07\n" +
                "END:VEVENT\n" +
                "BEGIN:VEVENT\n" +
                "DTSTAMP:20211115T013823Z\n" +
                "DTSTART:20220110T120000\n" +
                "DTEND:20220110T130000\n" +
                "SUMMARY:TST101 LEC-0101 Y\n" +
                "RRULE:FREQ=WEEKLY;UNTIL=20220411\n" +
                "UID:573c3237-38c1-4e8f-a809-ee669b744922\n" +
                "LOCATION:ROOM 08\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR\n";
    }

}
