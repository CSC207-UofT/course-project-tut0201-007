import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import net.fortuna.ical4j.validate.ValidationException;

/** Use Case class for exporting all sessions in a schedule into an ICS file */
public class ScheduleExporter {

    private LocalDate now = LocalDate.now();
    private int startYear = getStartYear(now.getYear(), now.getMonthValue());
    private final LocalDate FALL_SEMESTER_START_DATE = LocalDate.of(startYear, 9, 9);
    private final LocalDate FALL_SEMESTER_END_DATE = LocalDate.of(startYear, 12, 10);
    private final LocalDate WINTER_SEMESTER_START_DATE = LocalDate.of(startYear + 1, 1, 10);
    private final LocalDate WINTER_SEMESTER_END_DATE = LocalDate.of(startYear + 1, 4, 11);

    private File outputDirectory;

    public ScheduleExporter() {
        String baseDir = new File("").getAbsolutePath();
        outputDirectory = new File(baseDir.concat("/output"));
        if (!outputDirectory.exists()){
            outputDirectory.mkdir();
        }
    }

    /**
     * @param year current year
     * @param month current month
     * @return The year in which the current Academic Year started
     */
    // TODO: I don't know how to communicate what this does by function name better
    private int getStartYear(int year, int month) {
        if (month < 9) {
            return year - 1;
        } else {
            return year;
        }
    }

    /**
     * {@code writer} defaults to {@link FileWriter}
     *
     * @see ScheduleExporter#outputScheduleICS(Schedule, Writer)
     */
    public void outputScheduleICS(Schedule schedule) {
        try {
            Writer writer = new FileWriter(outputDirectory.getAbsolutePath().concat("/temp.ics"));
            outputScheduleICS(schedule, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts all Sessions within a Schedule into Events in .ics format, for use in a Calendar.
     * Allows for use of any Writer to allow exporting as a file, or storing as a string for
     * testing.
     *
     * @param schedule The Schedule object to output as ICS
     * @param writer The Writer object used to output the ICS, such as FileWriter or StringWriter
     */
    public void outputScheduleICS(Schedule schedule, Writer writer) {
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//CSC207 Team 007//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        for (Section tutorial : schedule.getTutorials()) {
            for (Timeslot timeslot: tutorial.getTimeslots()){
                addTimeslotToCalendar(tutorial.getName(), timeslot, calendar);
            }
        }
        for (Section lecture : schedule.getLectures()) {
            for (Timeslot timeslot: lecture.getTimeslots()){
                addTimeslotToCalendar(lecture.getName(), timeslot, calendar);
            }
        }
        try {
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addTimeslotToCalendar(String name, Timeslot timeslot, Calendar calendar){
        // TODO: have Sections store their semester, use that to decide which start date & end date
        // TODO: Also have sections store their course IDs & section IDs
        Date finalDay =
                new Date(
                        java.util.Date.from(
                                FALL_SEMESTER_END_DATE
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()));

        UidGenerator ug = new RandomUidGenerator();
        Uid uid = ug.generateUid();
        Recur recur = new Recur(Recur.Frequency.WEEKLY, finalDay);
        RRule recurrenceRule = new RRule(recur);

        LocalDate startDay = getStartingWeekDate(FALL_SEMESTER_START_DATE, timeslot.getDay());
        LocalDateTime start = startDay.atTime(timeslot.getStart());
        Date timeslotStart =
                new DateTime(java.util.Date.from(start.atZone(ZoneId.systemDefault()).toInstant()));

        LocalDateTime end = startDay.atTime(timeslot.getEnd());
        Date timeslotEnd =
                new DateTime(java.util.Date.from(end.atZone(ZoneId.systemDefault()).toInstant()));

        Location location = new Location(timeslot.getRoom());

        VEvent event = new VEvent(timeslotStart, timeslotEnd, name);
        event.getProperties().add(recurrenceRule);
        event.getProperties().add(uid);
        event.getProperties().add(location);

        calendar.getComponents().add(event);
    }

    private LocalDate getStartingWeekDate(LocalDate start, DayOfWeek dow) {
        LocalDate res = (LocalDate) dow.adjustInto(start);
        if (res.isBefore(start)) {
            return res.plusWeeks(1);
        } else {
            return res;
        }
    }

    public static void main(String[] args) throws IOException {
        ScheduleExporter exporter = new ScheduleExporter();
        Scheduler s = new Scheduler();
        ArrayList<String> courses = new ArrayList<>();
        courses.add("MAT237");
        Schedule schedule = s.createBasicSchedule(courses);
        System.out.println(schedule);
        exporter.outputScheduleICS(schedule);
    }
}
