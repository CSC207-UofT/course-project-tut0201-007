import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.*;
import java.util.ArrayList;

/**
 * Use Case class for exporting all sessions in a schedule into an ICS file
 */
public class ScheduleExporter {

    private LocalDate now = LocalDate.now();
    private int startYear = getStartYear(now.getYear(), now.getMonthValue());
    private final LocalDate FALL_SEMESTER_START_DATE = LocalDate.of(startYear, 9,9);
    private final LocalDate FALL_SEMESTER_END_DATE = LocalDate.of(startYear, 12,10);
    private final LocalDate WINTER_SEMESTER_START_DATE = LocalDate.of(startYear+1, 1,10);
    private final LocalDate WINTER_SEMESTER_END_DATE = LocalDate.of(startYear+1, 4,11);
    /**
     *
     * @param year current year
     * @param month current month
     * @return The year in which the current Academic Year started
     */
    private int getStartYear(int year, int month){
       if (month < 9){
           return year - 1;
       }
       else {
          return year;
       }
    }


    File outputDirectory;
    public ScheduleExporter(){
        String baseDir = new File("").getAbsolutePath();
        outputDirectory = new File(baseDir.concat("/output"));

    }

    public void outputScheduleICS(Schedule schedule) {
        //based on https://www.tutorialsbuddy.com/create-ics-calendar-file-in-java
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        Session tutorial = schedule.getTutorials().get(0);

            addSessionToCalendar(tutorial,calendar);

        FileWriter writer = null;

        try {
            writer = new FileWriter(outputDirectory.getAbsolutePath().concat("/temp.ics"));
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, writer);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void outputScheduleICS(Schedule schedule, Writer writer){

    }

    private void addSessionToCalendar(Session session, Calendar calendar){
        //TODO:
        /*
        How to convert to Date? maybe use recurrence relation like so https://stackoverflow.com/questions/44924292/how-to-create-recurring-event-ics-file-using-ical4j-in-java (DONE)
            - will need a bounds for the recurrence relation. Sessions need to know which term they are (F, S, Y) to create accurate ICS file
        Figure out what happens if you try and overwrite something that already exists (DONE)
        how to include location
        Unique name for schedule
        do Tests on output by using Writer

         */
        Date finalDay = new Date(java.util.Date.from(FALL_SEMESTER_END_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        UidGenerator ug = new RandomUidGenerator();
        Uid uid = ug.generateUid();
        Recur recur = new Recur(Recur.Frequency.WEEKLY, finalDay);
        RRule recurrenceRule = new RRule(recur);

        //TODO: have Sessions store their semester, use that to decide which start date & end date to use
        LocalDate startDay = getStartingWeekDate(FALL_SEMESTER_START_DATE, session.getSessionDay());
        LocalDateTime start = startDay.atTime(session.getSessionStartTime());
        Date sessionStart = new DateTime(java.util.Date.from(start.atZone(ZoneId.systemDefault()).toInstant()));

        LocalDateTime end = startDay.atTime(session.getSessionEndTime());
        Date sessionEnd = new DateTime(java.util.Date.from(end.atZone(ZoneId.systemDefault()).toInstant()));

        VEvent test = new VEvent(sessionStart, sessionEnd, session.getAssignedRoom());
        test.getProperties().add(recurrenceRule);
        test.getProperties().add(uid);

        calendar.getComponents().add(test);

    }
    public static LocalDate getStartingWeekDate(LocalDate start, DayOfWeek dow) {
        LocalDate res = (LocalDate) dow.adjustInto(start);
        System.out.println(res);
        if (res.isBefore(start)){
            return res.plusWeeks(1);
        }
        else{
            return res;
        }
    }

    public static void main(String[] args) {
        ScheduleExporter exporter = new ScheduleExporter();
        Scheduler s = new Scheduler();
        ArrayList<String> courses = new ArrayList<>();
        courses.add("MAT237");
        Schedule schedule = s.createBasicSchedule(courses);
        exporter.outputScheduleICS(schedule);


    }

}
