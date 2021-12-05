package workers;

import com.google.gson.*;
import entities.Course;
import entities.Section;
import entities.Timeslot;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
     * @param courseId the identifier for the course
     * @param session the semester that the course takes place ie S, F, Y
     * @return a Course object
     * @throws IOException if there is an issue with retrieving the course from the API
     */
    public static Course generateCourse(String courseId, char session) throws IOException {
        APIWorker apiWorker = new APIWorker(courseId);
        int w = 0;
        if (session == 'S' && apiWorker.semester.size() == 2) {
            w = 1;
        }
        JsonObject meetings =
                apiWorker
                        .info
                        .getAsJsonObject(apiWorker.semester.get(w))
                        .getAsJsonObject("meetings");

        List<Section> lectures = getSessionsByType(meetings, "LEC", courseId, session);
        List<Section> tutorials = getSessionsByType(meetings, "TUT", courseId, session);

        String exclusionsValue =
                apiWorker
                        .info
                        .getAsJsonObject(apiWorker.semester.get(w))
                        .get("exclusion")
                        .toString();
      
        List<String> exclusions = getCourseExclusions(exclusionsValue, courseId);

        String corequisitesValue =
                apiWorker
                        .info
                        .getAsJsonObject(apiWorker.semester.get(w))
                        .get("corequisite")
                        .toString();
      
        List<String> corequisites = getCourseCorequisites(corequisitesValue, courseId);

        return new Course(courseId, lectures, tutorials, session, exclusions, corequisites);
    }

    /**
     * Reads through a JSON object for a course and returns all of the sections of a given type
     *
     * @param meetings a JsonObject that corresponds to all of the meetings for a course
     * @param type LEC or TUT
     * @return an ArrayList of Section objects, each representing a given meeting
     */
    private static List<Section> getSessionsByType(
            JsonObject meetings, String type, String courseId, char session) {
        List<Section> specifiedSessions = new ArrayList<>();
        for (String meeting : meetings.keySet()) {
            if (meeting.contains(type) && !isCancelled(meetings, meeting)) {
                specifiedSessions.add(
                        createSection(
                                meetings.getAsJsonObject(meeting), meeting, courseId, session));
            }
        }
        return specifiedSessions;
    }

    /**
     * Extracts all course names from a string containing the course exclusions and puts it into an
     * ArrayList
     *
     * @param value a String that corresponds to all the exclusions for a course
     * @return an ArrayList of course names
     */
    public static List<String> getCourseExclusions(String value, String courseID) {
        List<String> shortenedCodes = new ArrayList<>();
        try {
            extractCodes(value, shortenedCodes);
        } catch (Exception IndexOutOfBoundsException) {
            System.out.println("The course " + courseID + " has no exclusions (empty string)");
        }
        return shortenedCodes;
    }

    /**
     * Extracts all course names from a string containing the course corequisites and puts it into
     * an ArrayList
     *
     * @param value a String that corresponds to all the corequisites for a course
     * @return an ArrayList of course names
     */
    public static List<String> getCourseCorequisites(String value, String courseID) {
        List<String> shortenedCodes = new ArrayList<>();
        try {
            extractCodes(value, shortenedCodes);
        } catch (Exception IndexOutOfBoundsException) {
            System.out.println("The course " + courseID + " has no corequisites (empty string)");
        }
        return shortenedCodes;
    }

    /**
     * Helper method for extracting the course code values from the API contents
     *
     * @param value a String that corresponds to all the corequisites for a course
     * @param shortenedCodes an empty arraylist to add the course codes to
     */
    private static void extractCodes(String value, List<String> shortenedCodes) {
        String cleanedValue = value.replace("\"", "").replace(".", "");
        List<String> values = List.of(cleanedValue.split("\\s*,\\s*"));
        for (String s : values) {
            shortenedCodes.add(s.substring(0, 6));
        }
    }

    /**
     * Creates a section for a given JsonObject
     *
     * @param meeting JsonObject corresponding to a section
     * @param name the name of the section
     * @return a Section object representing the JsonObject
     */
    private static Section createSection(
            JsonObject meeting, String name, String courseId, char session) {
        String fullName = String.format("%s %s %c", courseId, name, session);
        Section ret = new Section(fullName);
        JsonObject schedule = meeting.getAsJsonObject("schedule");
        String fallRoomKey = "assignedRoom1";
        String winterRoomKey = "assignedRoom2";

        for (String time : schedule.keySet()) {
            if (time.equals("-")) continue;
            JsonObject slot = schedule.getAsJsonObject(time);
            DayOfWeek day = toDay.get(slot.get("meetingDay").getAsString());
            LocalTime start = LocalTime.parse(slot.get("meetingStartTime").getAsString());
            LocalTime end = LocalTime.parse(slot.get("meetingEndTime").getAsString());
            if (session == 'F' || session == 'S') {
                String roomKey = session == 'S' ? winterRoomKey : fallRoomKey;
                String room = slot.get(roomKey).getAsString();
                if (room.equals("")) room = "ONLINE";
                ret.addTime(new Timeslot(start, end, day, room, session));
            } else if (session == 'Y') {
                String fallRoom = slot.get(fallRoomKey).getAsString();
                if (fallRoom.equals("")) {
                    fallRoom = "ONLINE";
                }
                String winterRoom = slot.get(winterRoomKey).getAsString();
                if (winterRoom.equals("")) {
                    winterRoom = "ONLINE";
                }

                ret.addTime(new Timeslot(start, end, day, fallRoom, 'F'));
                ret.addTime(new Timeslot(start, end, day, winterRoom, 'S'));
            }
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
