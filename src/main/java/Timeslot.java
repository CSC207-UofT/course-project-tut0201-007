import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.lang.Math;

public class Timeslot {
    private DayOfWeek day;
    private String room;
    private LocalTime start;
    private LocalTime end;

    public Timeslot(LocalTime start, LocalTime end, DayOfWeek day, String room) {
        this.start = start;
        this.end = end;
        this.day = day;
        this.room = room;
    }

    /**
     * getter method for room
     *
     * @return room that the session is held in
     */
    public String getRoom() {
        return room;
    }

    /**
     * getter method for day
     *
     * @return the day that a session is held
     */
    public DayOfWeek getDay() {
        return day;
    }

    /**
     * getter method for start
     *
     * @return start time for a given session
     */
    public LocalTime getStart() {
        return start;
    }

    /**
     * getter method for end
     *
     * @return end time for a given session
     */
    public LocalTime getEnd() {
        return end;
    }

    /**
     * method that checks if two timeslots overlap
     *
     * @param other timeslot to compare with this
     * @return true if there is a conflict
     */
    public Boolean conflictsWith(Timeslot other) {
        return this.day == other.day
                && !(this.start.isAfter(other.end) || other.start.isAfter(this.end));
    }

    public int subtract(Timeslot other) {
        if (this.day == other.day) {
            return (int) Math.max(Duration.between(this.end, other.start).toHours(),
                    Duration.between(this.start, other.end).toHours());
        }
        // might be a no no, idk
        return -1;
    }

    @Override
    public String toString() {
        return day.toString() + " from " + start.toString() + "-" + end.toString() + " at " + room;
    }
}
