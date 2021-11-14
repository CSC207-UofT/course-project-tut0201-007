package filters;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a filter that checks if all courses in a schedule occur within a range of
 * time. Can either restrict courses to the time range in general, or only restrict courses on a
 * certain day.
 */
public class TimeFilter implements Filter {

    private LocalTime lowerBound;
    private LocalTime upperBound;
    private Day filteredDay;

    /**
     * {@code filteredDay} defaults to Day.ALL_DAYS, meaning restriction to time range holds for all
     * days.
     *
     * @see TimeFilter#TimeFilter(LocalTime, LocalTime, Day)
     */
    public TimeFilter(LocalTime lowerBound, LocalTime upperBound) {
        this(lowerBound, upperBound, Day.ALL_DAYS);
    }

    /**
     * @param lowerBound inclusive Lower end of time range courses should be in.
     * @param upperBound Inclusive Upper bound of time range courses should be in
     * @param filteredDay Determine which day to restrict courses to time range for (Or all of them)
     */
    public TimeFilter(LocalTime lowerBound, LocalTime upperBound, Day filteredDay) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.filteredDay = filteredDay;
        // kinda abused notation on using Day enum here to include option to filter for all days
        // maybe better to have a boolean flag, but then I think it's confusing to select a day to
        // filter for
        // and also use a separate flag to say "Filter for all days"
    }

    /**
     * @param s Schedule to check
     * @return Whether all courses are within the time range
     */
    @Override
    public boolean checkSchedule(Schedule s) {
        if (s == null) {
            return false;
        }

        // only need timeslots for the checking
        List<Timeslot> timeslots = new ArrayList();

        for (Section lec : s.getLectures()) {
            timeslots.addAll(lec.getTimes());
        }

        for (Section tut : s.getTutorials()) {
            timeslots.addAll(tut.getTimes());
        }

        for (Timeslot timeslot : timeslots) {
            if (!withinBounds(timeslot, filteredDay)) {
                System.out.println(timeslot);
                return false;
            }
        }

        return true;
    }

    /**
     * @param timeslot Timeslot to check if it's within desired time range
     * @param filteredDay Day that time restriction is applied to
     * @return Whether the timeslot is fully within the desired time range (inclusive)
     */
    private boolean withinBounds(Timeslot timeslot, Day filteredDay) {
        if (filteredDay == Day.ALL_DAYS) {
            return lowerBound.compareTo(timeslot.getStart()) <= 0
                    && upperBound.compareTo(timeslot.getEnd()) >= 0;
        } else {
            return filteredDay.getDay() != timeslot.getDay()
                    || (lowerBound.compareTo(timeslot.getStart()) <= 0
                            && upperBound.compareTo(timeslot.getEnd()) >= 0);
        }
    }

    public enum Day {
        MONDAY(DayOfWeek.MONDAY),
        TUESDAY(DayOfWeek.TUESDAY),
        WEDNESDAY(DayOfWeek.WEDNESDAY),
        THURSDAY(DayOfWeek.THURSDAY),
        FRIDAY(DayOfWeek.FRIDAY),
        ALL_DAYS(null);

        private final DayOfWeek day;

        Day(DayOfWeek day) {
            this.day = day;
        }

        public DayOfWeek getDay() {
            return day;
        }
    }
}
