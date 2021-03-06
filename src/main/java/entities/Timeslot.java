package entities;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public class Timeslot implements Comparable<Timeslot> {
    private DayOfWeek day;
    private String room;
    private LocalTime start;
    private LocalTime end;
    private char session;

    /**
     * @param start Start of the timeslot
     * @param end End of the timeslot
     * @param day Day the timeslot occurs
     * @param room Room the room of the timeslot
     * @param session The session this timeslot occurs in (F/S). Note that this is different from a
     *     course's session, as the timeslot can only represent one "half" of a Y course's timeslot
     */
    public Timeslot(LocalTime start, LocalTime end, DayOfWeek day, String room, char session) {
        this.start = start;
        this.end = end;
        this.day = day;
        this.room = room;
        this.session = session;
    }

    /**
     * getter method for room
     *
     * @return room that the section is held in
     */
    public String getRoom() {
        return room;
    }

    /**
     * getter method for day
     *
     * @return the day that a section is held
     */
    public DayOfWeek getDay() {
        return day;
    }

    /**
     * getter method for start
     *
     * @return start time for a given section
     */
    public LocalTime getStart() {
        return start;
    }

    /**
     * getter method for end
     *
     * @return end time for a given section
     */
    public LocalTime getEnd() {
        return end;
    }

    /**
     * @return The session this timeslot is for (F/S). If the timeslot is from a Y course, session
     *     will be either F or S depending on which "half" of a section this timeslot represents
     */
    public char getSession() {
        return this.session;
    }

    /**
     * method that checks if two timeslots overlap
     *
     * @param other timeslot to compare with this
     * @return true if there is a conflict
     */
    public Boolean conflictsWith(Timeslot other) {
        return this.day == other.day
                && this.session == other.session
                && !(this.start.isAfter(other.end)
                        || this.start.equals(other.end)
                        || other.start.equals(this.end)
                        || other.start.isAfter(this.end));
    }

    /**
     * method that finds the maximum distance between two timeslots
     *
     * @param other timeslot to compare with this
     * @return int which is the hours between the two timeslots
     */
    public int getMaxDistance(Timeslot other) {
        if (this.day == other.day) {
            return (int)
                    Math.max(
                            Duration.between(this.end, other.start).toHours(),
                            Duration.between(this.start, other.end).toHours());
        }
        // might be a no no, idk
        return -1;
    }

    public String quickString() {
        return start.toString() + "-" + end.toString();
    }

    @Override
    public String toString() {
        return day.toString()
                + " from "
                + start.toString()
                + "-"
                + end.toString()
                + " at "
                + room
                + " in session "
                + session;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Timeslot) {
            Timeslot other = (Timeslot) o;
            return this.getDay().equals(other.getDay())
                    && this.getRoom().equals(other.getRoom())
                    && this.getStart().equals(other.getStart())
                    && this.getEnd().equals(other.getEnd())
                    && this.getSession() == other.getSession();
        }
        return false;
    }

    @Override
    public int compareTo(Timeslot that) {
        int sessionCompare;
        if (this.session == that.getSession()) {
            sessionCompare = 0;
            int dayCompare = this.day.compareTo(that.getDay());
            if (dayCompare == 0) {
                return this.start.compareTo(that.getStart());
            }
            return dayCompare;
        } else if (this.session == 'F' && that.getSession() == 'S') {
            // technically I can not do the second check, but imo it improves readability
            return -1;
        } else {
            return 1;
        }
    }
}
