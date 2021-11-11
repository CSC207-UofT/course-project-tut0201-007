import java.util.ArrayList;

/** This class represents a course, which consists of lecture and tutorial sessions */
public class Course {
    private final String courseId;
    private final char session;
    private final ArrayList<Section> lectures;
    private final ArrayList<Section> tutorials;

    /**
     * Constructs a course with an id and list of lecture and tutorial sessions
     *
     * @param courseId given id
     * @param lectures given lectures
     * @param tutorials given tutorials
     */
    public Course(
            String courseId,
            ArrayList<Section> lectures,
            ArrayList<Section> tutorials,
            char session) {
        this.courseId = courseId;
        this.lectures = lectures;
        this.tutorials = tutorials;
        this.session = session;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public ArrayList<Section> getLectures() {
        return lectures;
    }

    public ArrayList<Section> getTutorials() {
        return tutorials;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(courseId + "\nLectures:\n");
        String tab = "    ";
        for (Section lec : lectures) {
            ret.append(tab + lec.toString() + "\n");
        }
        ret.append("Tutorials:\n");
        for (Section tut : tutorials) {
            ret.append(tab + tut.toString() + "\n");
        }
        return ret.toString();
    }
}
