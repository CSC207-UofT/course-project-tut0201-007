package filters;

import entities.Course;
import entities.Schedule;

import java.util.List;

/**
 * This class represents a filter. It works by checking a given schedule for the given criteria, in this case
 * whether a schedule contains course corequisites or not
 */
public class CourseCoreqFilter implements Filter {
    private List<Course> courses;

    /**
     * Constructs a CourseCoreqFilter
     *
     * @param courses the courses given by the user on startup
     */
    public CourseCoreqFilter(List<Course> courses) {
        this.courses = courses;
    }

    /**
     * Returns true if the schedule contains courses that are listed as corequsities of each other
     *
     * @param s the checked schedule
     */
    @Override
    public boolean checkSchedule(Schedule s) {
        if (s == null) {
            return false;
        }

        for (Course course: this.courses) {
            // check if the given course has its corequisite in the schedule
            List<String> corequisites = course.getCorequisites();
            for (String courseId: s.getCourses()) {
                if (corequisites.contains(courseId)) {
                    return true;
                }
            }
        }
        return false;

    }

}
