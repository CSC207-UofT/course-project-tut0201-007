package filters;

import entities.Course;
import entities.Schedule;

import java.util.List;

/**
 * This class represents a filter. It works by checking a given schedule for the given criteria, in this case
 * whether a schedule contains course exclusions or not
 */
public class CourseExclusionFilter implements Filter {
    private List<Course> courses;

    /**
     * Constructs a CourseExclusionFilter
     *
     * @param courses the courses given by the user on startup
     */
    public CourseExclusionFilter(List<Course> courses) {
        this.courses = courses;
    }

    /**
     * Returns true if the schedule contains courses that are listed as exclusions of each other
     *
     * @param s the checked schedule
     */
    @Override
    public boolean checkSchedule(Schedule s) {

        if (s == null) {
            return false;
        }

        for (Course course: this.courses) {
            // check if the given course has its exclusion in the schedule
            List<String> exclusions = course.getExclusions();
            for (String courseId: s.getCourses()) {
                if (exclusions.contains(courseId)) {
                    return true;
                }
            }
        }
        return false;

    }
}