package workers;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import util.InvalidSessionException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CSVExporter extends Exporter{

    private int numFiles = 0;
    private final ZoneId zoneId = ZoneId.of("-4");
    private final LocalDate now = LocalDate.now(zoneId);
    private final int startYear = now.getMonthValue() < 9 ? now.getYear() - 1 : now.getYear();
    private final LocalDate FALL_SEMESTER_START_DATE = LocalDate.of(startYear, 9, 9);
    private final LocalDate FALL_SEMESTER_END_DATE = LocalDate.of(startYear, 12, 10);
    private final LocalDate WINTER_SEMESTER_START_DATE = LocalDate.of(startYear + 1, 1, 10);
    private final LocalDate WINTER_SEMESTER_END_DATE = LocalDate.of(startYear + 1, 4, 11);

    @Override
    public void outputSchedule(Schedule schedule) {
        String baseDir = new File("").getAbsolutePath();
        File outputDir = new File(baseDir.concat("/output"));
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
        try {
            Writer writer =
                    new FileWriter(
                            outputDir
                                    .getAbsolutePath()
                                    .concat("/CSVSchedule_" + numFiles + ".csv"));
            generateCSVSchedule(schedule, writer);
            numFiles += 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateCSVSchedule(Schedule schedule, Writer writer) {
        for (Section tutorial : schedule.getTutorials()) {
            for (Timeslot timeslot : tutorial.getTimes()) {
                try {
                    scheduleTimeslot(
                            tutorial.getName(), tutorial.getSession(), timeslot);
                } catch (InvalidSessionException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Section lecture : schedule.getLectures()) {
            for (Timeslot timeslot : lecture.getTimes()) {
                try {
                    scheduleTimeslot(
                            lecture.getName(), lecture.getSession(), timeslot);
                } catch (InvalidSessionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void scheduleTimeslot(String name, String session, Timeslot timeslot) throws InvalidSessionException {
            LocalDate termStartDate, termEndDate;
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

}