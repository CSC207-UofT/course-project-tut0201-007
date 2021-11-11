import com.google.gson.*;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

/** This class represents a Course Creator. This class uses APIWorker to generate Course objects. */
public class CourseCreator {

    private static final Map<String, DayOfWeek> toDay =
            Map.of(
                    "MO",
                    DayOfWeek.of(1),
                    "TU",
                    DayOfWeek.of(2),
                    "WE",
                    DayOfWeek.of(3),
                    "TH",
                    DayOfWeek.of(4),
                    "FR",
                    DayOfWeek.of(5));

    /**
     * Generates a course given a courseID and the session of the course
     *
     * @param courseId the identifier for hte course
     * @param session the semester that the course takes place ie S, F, Y
     * @return a Course object
     * @throws IOException i dont know what exception this throws lol
     */
    public static Course generateCourse(String courseId, char session) throws IOException {
        APIWorker apiWorker = new APIWorker(courseId);
        int w = 0;
        if (session == 'S') {
            w = 1;
        }
        JsonObject meetings =
                apiWorker
                        .info
                        .getAsJsonObject(apiWorker.semester.get(w))
                        .getAsJsonObject("meetings");
        ArrayList<Section> lectures = getSessionsByType(meetings, "LEC");
        ArrayList<Section> tutorials = getSessionsByType(meetings, "TUT");
        return new Course(courseId, lectures, tutorials, session);
    }

    /**
     * Reads through a JSON object for a course and returns all of the sessions of a given type
     *
     * @param meetings a JsonObject that corresponds to all of the meetings for a course
     * @param type LEC or TUT
     * @return an ArrayList of Section objects, each representing a given meeting
     */
    private static ArrayList<Section> getSessionsByType(JsonObject meetings, String type) {
        ArrayList<Section> specifiedSessions = new ArrayList<>();
        for (String meeting : meetings.keySet()) {
            if (meeting.contains(type) && !isCancelled(meetings, meeting)) {
                specifiedSessions.add(createSection(meetings.getAsJsonObject(meeting), meeting));
            }
        }
        return specifiedSessions;
    }

    /**
     * Creates a section for a given JsonObject
     *
     * @param meeting JsonObject corresponding to a section
     * @param name the name of the section
     * @return a Section object representing the JsonObject
     */
    private static Section createSection(JsonObject meeting, String name) {
        Section ret = new Section(name);
        JsonObject schedule = meeting.getAsJsonObject("schedule");
        for (String time : schedule.keySet()) {
            if (time.equals("-")) continue;
            JsonObject slot = schedule.getAsJsonObject(time);
            DayOfWeek day = toDay.get(slot.get("meetingDay").getAsString());
            LocalTime start = LocalTime.parse(slot.get("meetingStartTime").getAsString());
            LocalTime end = LocalTime.parse(slot.get("meetingEndTime").getAsString());
            String room = slot.get("assignedRoom1").getAsString();
            if (room.equals("")) room = "ONLINE";
            ret.addTime(new Timeslot(start, end, day, room));
        }
        return ret;
    }

    /**
     * Checks if a session is cancelled
     *
     * @param meetings a jsonObject of meetings that contains the session
     * @param meeting the session in question
     * @return true if the session is cancelled, otherwise false
     */
    private static boolean isCancelled(JsonObject meetings, String meeting) {
        return !meetings.getAsJsonObject(meeting).get("cancel").getAsString().equals("");
    }
}
