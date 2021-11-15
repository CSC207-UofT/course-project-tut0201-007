package controllers;

import entities.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import filters.Filter;
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
        //create our scheduler object
        Scheduler scheduler = new Scheduler();

        //get list of course ID strings
        List<String> courses = CommandLineInterface.promptUser();

        //course objects are instantiated based on the passed course codes
        List<Course> instantiatedCourses = Controller.courseInstantiator(courses);

        //get user specified filters, add them as filters to our scheduler object
        List<Filter> filters = CommandLineInterface.promptUserFilters(instantiatedCourses);
        scheduler.addFilters(filters);

        //call the permutation scheduler to give us all schedules given these courses and filters
        List<Schedule> schedules = scheduler.permutationScheduler(instantiatedCourses);

        //user interactive output method
        CommandLineInterface.displayUserSchedules(schedules);


    }

    /**
     * This method instantiates all courses with the courseCodes as their ID. Courses should be
     * sorted in terms of user priority
     *
     * @param courseCodes the coursecodes of the courses to be instantiated
     * @return Course objects matching the passed in CourseCodes
     */
    public static List<Course> courseInstantiator(List<String> courseCodes) {
        ArrayList<Course> courses = new ArrayList<>();
        for (String courseCode : courseCodes) {
            try {
                /**
                 * For every course code, generate the course from CourseCreator, add first lec/tut
                 * session to the lectures and tutorials within the schedule.
                 */
                Course newCourse = CourseCreator.generateCourse(courseCode, 'F');
                courses.add(newCourse);
            } catch (IOException exception) {
                /**
                 * In case something goes wrong with the API for a specific course code, we print
                 * the code and the exception that is thrown.
                 */
                System.out.println(
                        "Exception occurred for course "
                                + courseCode
                                + " with the following message: \n"
                                + exception.toString());
            }
        }
        return courses;
    }
}
