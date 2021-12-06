package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * A high-level entity responsible for representing the execution state of the program. It stores
 * user courses, next course being generate, user specified base schedule. This class is intended to
 * improve communication between boundaries in the program.
 */
public class ExecutionState {
    /**
     * @variable allUserCourses represents all Courses initially instantiated by the user
     * @variable remainingUserCourses represents all courses the user
     */
    private static boolean stateIsSetUp = false;

    private static GenerationMode generationMode;
    private static List<Course> allUserCourses;
    private static List<Course> remainingUserCourses;
    private static Course currentCourse;

    /**
     * Keeps track of list of all courses selected by user. This should be called only ONCE, when
     * the courses are first instantiated. If an empty list is passed, nothing happens.
     *
     * @param userCourses courses instantiated by user
     */
    public static void setUserCourses(List<Course> userCourses) {
        if (userCourses.size() == 0) {
            return;
        }
        allUserCourses = userCourses;
        remainingUserCourses = new ArrayList<>(allUserCourses);
        currentCourse = remainingUserCourses.remove(0);
        stateIsSetUp = true;
    }

    /** Returns all courses first instantiated by the user, so user can verify their selection. */
    public static List<Course> getUserCourses() {
        return allUserCourses;
    }

    /**
     * Gets course about to be used to generate the schedule.
     *
     * @return the current course
     */
    public static Course getCurrentCourse() {
        return currentCourse;
    }

    public static void setCurrentCourse(Course course) {
        currentCourse = course;
    }

    /**
     * Gets courses that still need to be included in schedule.
     *
     * @return the courses not included in schedules yet
     */
    public static List<Course> getRemainingCourses() {
        return new ArrayList<>(remainingUserCourses);
    }

    /**
     * Getters and setters for GenerationMode used in Controller and CommandLineInterface
     *
     * @return the current generation mode
     */
    public static GenerationMode getGenerationMode() {
        return generationMode;
    }

    public static void setGenerationMode(GenerationMode mode) {
        generationMode = mode;
    }

    public static boolean isSetUp() {
        return stateIsSetUp;
    }

    public enum GenerationMode {
        ALL_PERMUTATIONS(0),
        ONE_BY_ONE(1);

        private final int generationMode;

        GenerationMode(int i) {
            this.generationMode = i;
        }

        public int getDay() {
            return generationMode;
        }
    }
}
