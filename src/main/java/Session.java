import java.time.DayOfWeek;
import java.time.LocalTime;

/** This class represents a session, such as a single lecture or tutorial */
public class Session implements Comparable<Session> {

    private String type;
    private String room;
    private String section;
    private LocalTime start;
    private LocalTime end;
    private DayOfWeek day;

    /**
     * This Builder class allows different kinds of sessions to be built, to allow for flexibility
     */
    public static class Builder {

        private final String type;
        private String room = "";
        private String section = "";
        private LocalTime start = LocalTime.MIN;
        private LocalTime end = LocalTime.MIN;
        private DayOfWeek day = DayOfWeek.SATURDAY;

        public Builder(String type) {
            this.type = type;
        }

        public Builder inRoom(String room) {
            this.room = room;
            return this;
        }

        public Builder inSection(String section) {
            this.section = section;
            return this;
        }

        public Builder startsAt(LocalTime start) {
            this.start = start;
            return this;
        }

        public Builder endsAt(LocalTime end) {
            this.end = end;
            return this;
        }

        public Builder onDay(DayOfWeek day) {
            this.day = day;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }

    /**
     * Constructs a Session
     *
     * @param builder a session builder
     */
    public Session(Builder builder) {
        type = builder.type;
        room = builder.room;
        section = builder.section;
        start = builder.start;
        end = builder.end;
        day = builder.day;
    }

    /**
     * Gets the type of the session.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Gets the section of the session.
     */
    public String getSection() {
        return this.type;
    }

    /** Returns a string representation of the session */
    @Override
    public String toString() {
        return this.type
                + " section "
                + this.section
                + " in "
                + this.room
                + " from "
                + this.start.toString()
                + " to "
                + this.end.toString()
                + " on "
                + this.day;
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
        if (!this.day.equals(other.day)) {
            return this.day.compareTo(other.day);
        } else if (this.end.isBefore(other.start)) {
            return -1;
        } else if (this.start.isAfter(other.end)) {
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
