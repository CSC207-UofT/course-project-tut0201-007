package filters;

import java.time.DayOfWeek;

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

    @Override
    public String toString() {
        switch (day) {
            case MONDAY:
                return "Monday";
            case TUESDAY:
                return "Tuesday";
            case WEDNESDAY:
                return "Wednesday";
            case THURSDAY:
                return "Thursday";
            case FRIDAY:
                return "Friday";
        }
        return "Everyday";
    }
}
