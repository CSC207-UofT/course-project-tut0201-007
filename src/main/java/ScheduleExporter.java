import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Use Case class for exporting all sessions in a schedule into an ICS file
 */
public class ScheduleExporter {
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
        How to convert to Date? maybe use recurrence relation like so https://stackoverflow.com/questions/44924292/how-to-create-recurring-event-ics-file-using-ical4j-in-java
            - will need a bounds for the recurrence relation. Maybe sessions need to know which term they are (F, S, Y) to create accurate ICS file
        Figure out what happens if you try and overwrite something that already exists
        how to include location
        Unique name for schedule
        do Tests on output by using Writer

         */
        UidGenerator ug = new RandomUidGenerator();
        Uid uid = ug.generateUid();
        Recur recur = new Recur(Recur.Frequency.WEEKLY, 15);
        RRule recurrenceRule = new RRule(recur);
        java.util.Date today = java.util.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        VEvent test = new VEvent( new Date(today), session.getAssignedRoom() );
//        test.getProperties().add(recurrenceRule);
        test.getProperties().add(uid);

        calendar.getComponents().add(test);

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
