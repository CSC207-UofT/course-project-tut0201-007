package controllers;

import entities.*;
import filters.Filter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import workers.*;

/**
 * The main class of the program. Manages user input, scheduling, adds filters, and negotiates
 * output.
 */
public class Controller {

    public static void main(String[] args) {
        /**
         * We create the input getter, and scheduler. Note that scheduler has addFilters() method,
         * which we use according to User Input later in this method
         */
        // create our scheduler object
        Scheduler scheduler = new Scheduler();

        /**
         * Check if user wants to import or create new schedules. 1 -> new schedules 0 -> import -1
         * -> do nothing
         */
        int userStrategy = CommandLineInterface.promptUser();
        if (userStrategy == -1) {
            return;
        } else if (userStrategy == 0) {
            Schedule baseSchedule = CommandLineInterface.promptImportSchedule();
            scheduler.setBaseSchedule(baseSchedule);
        }
        // ask user for course codes
        List<String> courses = CommandLineInterface.promptCourseCodeNames();

        // course objects are instantiated based on the passed course codes
        List<Course> instantiatedCourses = Controller.courseInstantiator(courses);

        // get user specified filters, add them as filters to our scheduler object
        List<Filter> filters = CommandLineInterface.promptUserFilters(instantiatedCourses);
        scheduler.addFilters(filters);

        // call the scheduler to give us all schedules given these courses, filters, and base
        // schedule
        List<Schedule> schedules = scheduler.permutationScheduler(instantiatedCourses);

        // user interactive output method
        CommandLineInterface.displayUserSchedules(schedules);
    }

    /**
     * This method instantiates all courses with the courseCodes as their ID. Courses should be
     * sorted in terms of user priority
     *
     * @param courseInputs the Course Inputs for the courses to be instantiated. Assumed format is
     *     something like TST101F
     * @return Course objects matching the passed in CourseCodes
     */
    public static List<Course> courseInstantiator(List<String> courseInputs) {
        ArrayList<Course> courses = new ArrayList<>();
        for (String courseInput : courseInputs) {
            try {
                String courseCode = courseInput.substring(0, 6);
                char session = Character.toUpperCase(courseInput.charAt(6));
                /**
                 * For every course code, generate the course from CourseCreator, add first lec/tut
                 * session to the lectures and tutorials within the schedule.
                 */
                Course newCourse = CourseCreator.generateCourse(courseCode, session);
                courses.add(newCourse);

            } catch (IOException | IllegalStateException exception) {
                /**
                 * In case something goes wrong with the API for a specific course code, we print
                 * the code and the exception that is thrown.
                 */
                System.out.println(
                        "Exception occurred for course "
                                + courseInput
                                + " with the following message: \n"
                                + exception.toString());
            }
        }
        return courses;
    }
}
