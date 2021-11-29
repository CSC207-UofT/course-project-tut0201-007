package workers;

import entities.Schedule;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * An abstract class that provides a template for exporting schedules to different file types (i.e.
 * ICS, CSV, etc.)
 */
public abstract class Exporter {

     int num = 0;
     final ZoneId zoneId = ZoneId.of("-4");
     final LocalDate now = LocalDate.now(zoneId);
     final int startYear = now.getMonthValue() < 9 ? now.getYear() - 1 : now.getYear();
     final int endYear = startYear + 1;
     final LocalDate FALL_SEMESTER_START_DATE = LocalDate.of(startYear, 9, 9);
     final LocalDate FALL_SEMESTER_END_DATE = LocalDate.of(startYear, 12, 10);
     final LocalDate WINTER_SEMESTER_START_DATE = LocalDate.of(endYear, 1, 10);
     final LocalDate WINTER_SEMESTER_END_DATE = LocalDate.of(endYear, 4, 11);

    public Exporter() {}

    /**
     * Get the specific date that a course will start
     *
     * @param termStart The start of the term for this course
     * @param dow The day of the week that the Timeslot is
     * @return The Date that the course will start
     */
    public static LocalDate getStartingWeekDate(LocalDate termStart, DayOfWeek dow) {
        LocalDate res = (LocalDate) dow.adjustInto(termStart);
        if (res.isBefore(termStart)) {
            return res.plusWeeks(1);
        } else {
            return res;
        }
    }

    /**
     * An abstract method requiring that any exporter class has a method to output a schedule as a
     * file.
     *
     * @param schedule The Schedule object to output as some file
     */
    public abstract void outputSchedule(Schedule schedule);
}
