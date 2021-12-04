package entities;

import java.util.List;

/** This class represents a course, which consists of lecture and tutorial sessions */
public class Course {
    private final String courseId;
    private final char session;
    private final List<Section> lectures;
    private final List<Section> tutorials;
    private final List<String> courseExclusionIds;

    /**
     * Constructs a course with an id and list of lecture and tutorial sessions as well as course
     * exclusions.
     *
     * @param courseId given id
     * @param lectures given lectures
     * @param tutorials given tutorials
     * @param session given session
     * @param exclusions given course exclusions
     */
    public Course(
            String courseId,
            List<Section> lectures,
            List<Section> tutorials,
            char session,
            List<String> exclusions) {
        this.courseId = courseId;
        this.lectures = lectures;
        this.tutorials = tutorials;
        this.session = session;
        this.courseExclusionIds = exclusions;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public List<Section> getLectures() {
        return lectures;
    }

    public List<Section> getTutorials() {
        return tutorials;
    }

    public List<String> getExclusions() {
        return courseExclusionIds;
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
