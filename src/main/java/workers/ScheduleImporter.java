package workers;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.util.MapTimeZoneCache;

/** Use Case class for importing a Schedule from an ICS file */
public class ScheduleImporter {

    /**
     * Parses information written in ICS format using Reader, and converts schedule from the ICS
     * file into a Schedule object
     *
     * @param reader The Reader object that parses from the ICS file or ICS formatted data
     * @return A Schedule object that contains all the Sections from the ICS file
     */
    public static Schedule importSchedule(Reader reader) {
        Schedule schedule = new Schedule();
        Map<String, Section> sectionsByName = new HashMap<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

        // Disable feature we don't need
        System.setProperty(
                "net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());
        CalendarBuilder builder = new CalendarBuilder();
        Calendar cal = null;
        try {
            cal = builder.build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }

        for (Object o : cal.getComponents("VEVENT")) {
            Component event = (Component) o;
            String location = event.getProperty("LOCATION").getValue();
            String title = event.getProperty("SUMMARY").getValue();
            String startTimeString = event.getProperty("DTSTART").getValue();
            String endTimeString = event.getProperty("DTEND").getValue();

            LocalDateTime start = LocalDateTime.parse(startTimeString, dateFormatter);
            LocalDateTime end = LocalDateTime.parse(endTimeString, dateFormatter);

            Timeslot timeslot =
                    new Timeslot(
                            start.toLocalTime(), end.toLocalTime(), start.getDayOfWeek(), location);
            sectionsByName.putIfAbsent(title, new Section(title));
            sectionsByName.get(title).addTime(timeslot);
        }

        for (Section section : sectionsByName.values()) {
            if (section.getName().contains("LEC")) {
                schedule.addLecture(section);
            } else if (section.getName().contains("TUT")) {
                schedule.addTutorial(section);
            }
        }
        return schedule;
    }
}
