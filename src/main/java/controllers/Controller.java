package controllers;

import entities.*;
import filters.Filter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        CommandLineInterface CLI = new CommandLineInterface(1);

        /**
         * Check if user wants to import or create new schedules.
         * 1 -> new schedules
         * 0 -> import
         * -1 -> do nothing
         */
        int userStrategy = CLI.promptUser();
        if (userStrategy == -1) {
            return;
        } else if (userStrategy == 0) {
            Schedule baseSchedule = CLI.promptImportSchedule();
            scheduler.setBaseSchedule(baseSchedule);
        }
        // ask user for course codes
        List<String> courses = CLI.promptCourseCodeNames();
        // course objects are instantiated based on the passed course codes
        List<Course> instantiatedCourses = Controller.courseInstantiator(courses);

        // get user specified filters, add them as filters to our scheduler object
        List<Filter> filters = CLI.promptUserFilters(instantiatedCourses);
        scheduler.addFilters(filters);

        /**
         * ask user if they want one by one schedule generation.
         *  1 - yes
         *  0 - no
         */
        CLI.confirmOneByOneGeneration();

        /**
         * while the user wants one by one generation, keep repeating
         */
        //final user schedules
        List<Schedule> schedules = new ArrayList<>();

        while (CLI.getGenerationMode() == 1 || instantiatedCourses.size() > 1) {
            Course nextCourse = instantiatedCourses.get(0);
            List<Schedule> nextCourseSchedules = scheduler.permutationScheduler(nextCourse);
            Schedule nextBase = CLI.promptUserBaseSchedule(nextCourseSchedules);
            if (nextBase == null) {
                CLI.setGenerationMode(0);
            } else {
                instantiatedCourses.remove(0);
                scheduler.setBaseSchedule(nextBase);
            }
            if (instantiatedCourses.size() == 0) {
                CLI.setGenerationMode(0);
            }
        }

        CLI.setGenerationMode(0);

        // call the scheduler to give us all schedules given these courses, filters, and base
        // schedule
        if (instantiatedCourses.size() != 0) {
            schedules = scheduler.permutationScheduler(instantiatedCourses);
        } else {
            schedules = new ArrayList<>(List.of(scheduler.getBaseSchedule()));
        }

        // user interactive output method
        CLI.displayUserSchedules(schedules);
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

            } catch (IOException exception) {
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
