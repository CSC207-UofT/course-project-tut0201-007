package workers;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import util.InvalidSessionException;

/** Use Case class for exporting a Schedule into an ICS file */
public class ICSExporter extends Exporter {

    private int numFiles = 0;
    private ZoneId zoneId = ZoneId.of("-4");
    private LocalDate now = LocalDate.now(zoneId);
    private int startYear = now.getMonthValue() < 9 ? now.getYear() - 1 : now.getYear();
    private final LocalDate FALL_SEMESTER_START_DATE = LocalDate.of(startYear, 9, 9);
    private final LocalDate FALL_SEMESTER_END_DATE = LocalDate.of(startYear, 12, 10);
    private final LocalDate WINTER_SEMESTER_START_DATE = LocalDate.of(startYear + 1, 1, 10);
    private final LocalDate WINTER_SEMESTER_END_DATE = LocalDate.of(startYear + 1, 4, 11);
    private Calendar calendar;
    private String baseDir;
    private File outputDirectory;

    public ICSExporter() {
        calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//CSC207 Team 007//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        baseDir = new File("").getAbsolutePath();
        outputDirectory = new File(baseDir.concat("/output"));
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }
    }

    /**
     * {@code writer} defaults to {@link FileWriter}
     *
     * @see ICSExporter#outputSchedule(Schedule, Writer)
     */
    @Override
    public void outputSchedule(Schedule schedule) {
        try {
            Writer writer =
                    new FileWriter(
                            outputDirectory
                                    .getAbsolutePath()
                                    .concat("/schedule" + numFiles + ".ics"));
            outputSchedule(schedule, writer);
            numFiles += 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts all Timeslots within a Schedule into Events in .ics format, for use in a Calendar.
     * Allows for use of any Writer to allow exporting as a file, or storing as a string for
     * testing.
     *
     * @param schedule The Schedule object to output as ICS
     * @param writer The Writer object used to output the ICS, such as FileWriter or StringWriter
     */
    public void outputSchedule(Schedule schedule, Writer writer) {

        for (Section tutorial : schedule.getTutorials()) {
            for (Timeslot timeslot : tutorial.getTimes()) {
                try {
                    addTimeslotToCalendar(tutorial.getName(), tutorial.getSession(), timeslot);
                } catch (InvalidSessionException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Section lecture : schedule.getLectures()) {
            for (Timeslot timeslot : lecture.getTimes()) {
                try {
                    addTimeslotToCalendar(lecture.getName(), lecture.getSession(), timeslot);
                } catch (InvalidSessionException e) {
                    e.printStackTrace();
                }
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

    /**
     * Create VEvent corresponding to Timeslot, and add to Calendar
     *
     * @param name Timeslot's corresponding Course code, section, and session. Ex (MAT237 LEC0101 Y)
     * @param session The session the Timeslot occurs in (F/S/Y)
     * @param timeslot Timeslot to create VEvent from
     */
    private void addTimeslotToCalendar(String name, String session, Timeslot timeslot)
            throws InvalidSessionException {
        LocalDate termStartDate = FALL_SEMESTER_START_DATE;
        LocalDate termEndDate = FALL_SEMESTER_END_DATE;
        switch (session) {
            case "F":
                termStartDate = FALL_SEMESTER_START_DATE;
                termEndDate = FALL_SEMESTER_END_DATE;
                break;
            case "S":
                termStartDate = WINTER_SEMESTER_START_DATE;
                termEndDate = WINTER_SEMESTER_END_DATE;
                break;
            case "Y":
                termStartDate = FALL_SEMESTER_START_DATE;
                termEndDate = WINTER_SEMESTER_END_DATE;
                break;
            default:
                throw new InvalidSessionException(
                        String.format(
                                "%s has invalid session %s. It should be either F, Y, or S",
                                name, session));
        }
        Date finalDay = new Date(java.util.Date.from(termEndDate.atStartOfDay(zoneId).toInstant()));

        UidGenerator ug = new RandomUidGenerator();
        Uid uid = ug.generateUid();
        Recur recur = new Recur(Recur.Frequency.WEEKLY, finalDay);
        RRule recurrenceRule = new RRule(recur);

        LocalDate startDay = getStartingWeekDate(termStartDate, timeslot.getDay());
        LocalDateTime start = startDay.atTime(timeslot.getStart());
        Date timeslotStart = new DateTime(java.util.Date.from(start.atZone(zoneId).toInstant()));

        LocalDateTime end = startDay.atTime(timeslot.getEnd());
        Date timeslotEnd = new DateTime(java.util.Date.from(end.atZone(zoneId).toInstant()));

        Location location = new Location(timeslot.getRoom());

        VEvent event = new VEvent(timeslotStart, timeslotEnd, name);
        event.getProperties().add(recurrenceRule);
        event.getProperties().add(uid);
        event.getProperties().add(location);

        calendar.getComponents().add(event);
    }
}
