package workers;

import entities.Schedule;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * An abstract class that provides a template for exporting schedules to different file types (i.e.
 * ICS, CSV, etc.)
 */
public abstract class Exporter {

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
