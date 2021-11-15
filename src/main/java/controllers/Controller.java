package controllers;

import entities.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Scheduler scheduler = new Scheduler();

        List<String> courses = CommandLineInterface.promptUser();

        List<Course> instantiatedCourses = Controller.courseInstantiator(courses);

        List<Schedule> schedules = scheduler.permutationScheduler(instantiatedCourses);

        for (Schedule sch : schedules) {
            System.out.println(sch);
        }
    }

    /**
     * This method instantiates all courses with the courseCodes as their ID. Courses should be
     * sorted in terms of user priority
     *
     * @param courseInputs the Course Inputs for the courses to be instantiated. Assumed format is something like TST101F
     * @return Course objects matching the passed in CourseCodes
     */
    public static List<Course> courseInstantiator(List<String> courseInputs) {
        ArrayList<Course> courses = new ArrayList<>();
        Pattern validInput = Pattern.compile("^[a-zA-Z0-9]{6}[fsyFSY]");
        for (String courseInput : courseInputs) {
            Matcher matcher = validInput.matcher(courseInput);
            try {
                if(matcher.find()){
                    String courseCode = courseInput.substring(0, 6);
                    char session = Character.toUpperCase(courseInput.charAt(6));
                    /**
                     * For every course code, generate the course from CourseCreator, add first lec/tut
                     * session to the lectures and tutorials within the schedule.
                     */
                    Course newCourse = CourseCreator.generateCourse(courseCode, session);
                    courses.add(newCourse);

                }
                else{
                    System.out.printf("Invalid course input of %s", courseInput);
                }
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
