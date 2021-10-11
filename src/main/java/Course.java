import java.util.ArrayList;

/**
 * This class represents a course, which consists of lecture and tutorial sessions
 */
public class Course {
    private final int courseId;
    private final ArrayList<Session> lectures;
    private final ArrayList<Session> tutorials;

    /**
     * Constructs a course with an id and list of lecture and tutorial sessions
     * @param courseId given id
     * @param lectures given lectures
     * @param tutorials given tutorials
     */
    public Course(int courseId, ArrayList<Session> lectures, ArrayList<Session> tutorials) {
        this.courseId = courseId;
        this.lectures = lectures;
        this.tutorials = tutorials;
    }
}
