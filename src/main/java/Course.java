import java.util.ArrayList;

/** This class represents a course, which consists of lecture and tutorial sessions */
public class Course {
    private final String courseId;
    private final ArrayList<Session> lectures;
    private final ArrayList<Session> tutorials;

    /**
     * Constructs a course with an id and list of lecture and tutorial sessions
     *
     * @param courseId given id
     * @param lectures given lectures
     * @param tutorials given tutorials
     */
    public Course(String courseId, ArrayList<Session> lectures, ArrayList<Session> tutorials) {
        this.courseId = courseId;
        this.lectures = lectures;
        this.tutorials = tutorials;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public ArrayList<Session> getLectures() {
        return this.lectures;
    }

    public ArrayList<Session> getTutorials() {
        return this.tutorials;
    }
}
