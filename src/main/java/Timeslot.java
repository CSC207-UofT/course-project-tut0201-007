import org.joda.time.LocalTime;

public class Timeslot {
    // day is represented by int from 0-6, with 0 being Sunday
    private int day;
    private String room;
    private LocalTime start;
    private LocalTime end;

    public Timeslot(LocalTime start, LocalTime end, int day, String room) {
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
    public int getDay() {
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
     * @return true if there is not a conflict
     */
    public Boolean checkConflict(Timeslot other) {
        return other.getDay() != day || (other.getStart().isAfter(end) || start.isAfter(other.getEnd()));
    }
}
