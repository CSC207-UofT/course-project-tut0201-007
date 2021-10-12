/**
 * This class represents a session, which occupies a timeslot on a schedule
 */
public class Session {

    //TODO: Add an instance variable representing the time of the Session.
    private final String sessionId;
    private final String assignedRoom;

    /**
     * Constructs a session with an id and location
     * @param sessionId given id
     * @param assignedRoom given location
     */
    public Session(String sessionId, String assignedRoom) {
        this.sessionId = sessionId;
        this.assignedRoom = assignedRoom;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return this.sessionId + " in " + this.assignedRoom;
        //TODO: Add the time to the representation
    }

    //TODO: Make these comparable with respect to time.
    /**
     * If s1, s2 are sessions, then
     * s1 < s2 iff s1 is before s2, and they do not conflict
     * s1 == s2 iff s1 and s2 conflict
     * s1 > s2 iff s1 is after s2 and they do not conflict.
     */
}
