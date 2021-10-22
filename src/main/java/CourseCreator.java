import com.google.gson.*;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/** This class represents a Course Creator. This class uses APIWorker to generate Course objects. */
public class CourseCreator {

    private static final Map<String, DayOfWeek> toDay =
            Map.of(
                    "MO", DayOfWeek.of(1),
                    "TU", DayOfWeek.of(2),
                    "WE", DayOfWeek.of(3),
                    "TH", DayOfWeek.of(4),
                    "FR", DayOfWeek.of(5));

    /**
     * Generates a course given a courseId.
     *
     * @param courseId the id of the course to be created.
     * @return a Course object
     */
    public Course generateCourse(String courseId) throws IOException {
        APIWorker apiWorker = new APIWorker(courseId);
        String courseCode =
                apiWorker
                        .info
                        .get(apiWorker.semester.get(0))
                        .getAsJsonObject()
                        .get("code")
                        .getAsString();
        ArrayList<Session> lectures = getSessionsByType(apiWorker, "LEC");
        ArrayList<Session> tutorials = getSessionsByType(apiWorker, "TUT");
        return new Course(courseCode, lectures, tutorials);
    }

    /**
     * Reads through the JSON and returns all the sessions of a given type.
     *
     * @param apiWorker the APIWorker for the given course
     * @param type the type of session, either a lecture or tutorial
     * @return an ArrayList of Session objects
     */
    private static ArrayList<Session> getSessionsByType(APIWorker apiWorker, String type) {
        ArrayList<Session> specifiedSessions = new ArrayList<>();
        JsonObject meetings =
                apiWorker
                        .info
                        .get(apiWorker.semester.get(0))
                        .getAsJsonObject()
                        .get("meetings")
                        .getAsJsonObject();
        List<String> meetingsOfType =
                meetings.keySet().stream()
                        .filter(a -> a.contains(type))
                        .collect(Collectors.toList());
        for (String meeting : meetingsOfType) {
            if (!isCancelled(meetings, meeting) && !isAsynchronous(meetings, meeting)) {
                JsonObject timeslots =
                        meetings.get(meeting).getAsJsonObject().get("schedule").getAsJsonObject();
                for (String timeslot : timeslots.keySet()) {
                    specifiedSessions.add(
                            generateSession(type, timeslots.get(timeslot).getAsJsonObject()));
                }
            }
        }

        if (specifiedSessions.isEmpty() && !meetingsOfType.isEmpty()) {
            specifiedSessions.add(new Session.Builder(type).inRoom("ONLINE").build());
        }
        return specifiedSessions;
    }

    /**
     * Creates a session given a JsonObject
     *
     * @param type the type of session, either a lecture or tutorial
     * @param timeslot the provided timeslot, which is a JsonObject containing information about the
     *     session
     * @return a Session object
     */
    private static Session generateSession(String type, JsonObject timeslot) {
        String room = timeslot.get("assignedRoom1").getAsString();
        return new Session.Builder(type)
                .inRoom(Objects.equals(room, "") ? "ONLINE" : room)
                .startsAt(LocalTime.parse(timeslot.get("meetingStartTime").getAsString()))
                .endsAt(LocalTime.parse(timeslot.get("meetingEndTime").getAsString()))
                .onDay(toDay.get(timeslot.get("meetingDay").getAsString()))
                .build();
    }

    /**
     * Checks if a session is cancelled
     *
     * @param meetings a jsonObject of meetings that contains the session
     * @param meeting the session in question
     * @return true if the session is cancelled, otherwise false
     */
    private static boolean isCancelled(JsonObject meetings, String meeting) {
        return !meetings.get(meeting).getAsJsonObject().get("cancel").getAsString().equals("");
    }

    /**
     * Checks if a session is asynchronous
     *
     * @param meetings a jsonObject of meetings that contains the session
     * @param meeting the session in question
     * @return true if the session is asynchronous, otherwise false
     */
    private static boolean isAsynchronous(JsonObject meetings, String meeting) {
        return meetings.get(meeting)
                .getAsJsonObject()
                .get("deliveryMode")
                .getAsString()
                .equals("ONLASYNC");
    }
}
