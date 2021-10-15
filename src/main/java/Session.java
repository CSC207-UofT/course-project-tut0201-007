import java.time.DayOfWeek;
import java.time.LocalTime;

/** This class represents a session, which occupies a timeslot on a schedule */
public class Session implements Comparable<Session> {

    private final String sessionType;
    private final String assignedRoom;
    private final LocalTime sessionStartTime;
    private final LocalTime sessionEndTime;
    private final DayOfWeek sessionDay;

    /**
     * Constructs a session with an id and location
     *
     * @param sessionType given Type (lec/tut)
     * @param assignedRoom given location (room code)
     * @param sessionStartTime given start time
     * @param sessionEndTime given end time
     * @param sessionDay given day
     */
    public Session(
            String sessionType,
            String assignedRoom,
            LocalTime sessionStartTime,
            LocalTime sessionEndTime,
            DayOfWeek sessionDay) {
        this.sessionType = sessionType;
        this.assignedRoom = assignedRoom;
        this.sessionStartTime = sessionStartTime;
        this.sessionEndTime = sessionEndTime;
        this.sessionDay = sessionDay;
    }

    /** Returns a string representation of the session */
    @Override
    public String toString() {
        return this.sessionType
                + " in "
                + this.assignedRoom
                + " from "
                + this.sessionStartTime.toString()
                + " to "
                + this.sessionEndTime.toString()
                + " on "
                + this.sessionDay;
    }

    /**
     * Compares this session with another session for order.
     *
     * <p>Returns a negative integer, zero, or a positive integer if this session is before,
     * overlapping, or after than the other session.
     *
     * @param other the object to be compared.
     * @return a negative integer, zero, or a positive integer if this session is before,
     *     overlapping, or after than the other session.
     */
    public int compareTo(Session other) {
        if (!this.sessionDay.equals(other.sessionDay)) {
            return this.sessionDay.compareTo(other.sessionDay);
        } else if (this.sessionEndTime.isBefore(other.sessionStartTime)) {
            return -1;
        } else if (this.sessionStartTime.isAfter(other.sessionEndTime)) {
            return 1;
        } else {
            return 0;
        }
    }
    /**
     * If s1, s2 are sessions, then s1 < s2 iff s1 is before s2, and they do not conflict s1 == s2
     * iff s1 and s2 conflict s1 > s2 iff s1 is after s2 and they do not conflict.
     */
}
