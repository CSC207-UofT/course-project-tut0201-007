/**
 * This class represents a session, which occupies a timeslot on a schedule
 */
public class Session {
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
}
