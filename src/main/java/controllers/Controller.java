package controllers;

import entities.*;
import filters.Filter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import util.ConsoleColours;
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
        ExecutionState.GenerationMode oneByOne = ExecutionState.GenerationMode.ONE_BY_ONE;
        ExecutionState.GenerationMode allPermutations =
                ExecutionState.GenerationMode.ALL_PERMUTATIONS;

        ExecutionState.setGenerationMode(oneByOne);
        CommandLineInterface CLI = new CommandLineInterface(oneByOne);

        /**
         * Check if user wants to import or create new schedules. 1 -> new schedules 0 -> import -1
         * -> do nothing
         */
        int userStrategy = CLI.promptUser();
        if (userStrategy == -1) {
            return;
        } else if (userStrategy == 0) {
            Schedule baseSchedule = CLI.promptImportSchedule();
            scheduler.setBaseSchedule(baseSchedule);
        }

        List<String> courses;
        List<Course> instantiatedCourses = new ArrayList<>();

        // Prompt user for courses until courses are successfully instantiated (no
        // issues with API
        // retrieving courses)
        while (!ExecutionState.isSetUp()) {
            // ask user for course codes
            courses = CLI.promptCourseCodeNames();
            // course objects are instantiated based on the passed course codes
            instantiatedCourses = Controller.courseInstantiator(courses);
            // declare these as the courses for user
            ExecutionState.setUserCourses(instantiatedCourses);
        }

        // Display course information
        if (userStrategy == 2) {
            CLI.promptCourseInfoDisplay(instantiatedCourses);
        }

        // get user specified filters, add them as filters to our scheduler object
        List<Filter> filters = CLI.promptUserFilters(instantiatedCourses);
        scheduler.addFilters(filters);

        /** ask user if they want one by one schedule generation. */
        CLI.selectGenerationMode();

        /** while the user wants one by one generation, keep repeating */
        // final user schedules
        List<Schedule> schedules;

        while (ExecutionState.getGenerationMode() == oneByOne && instantiatedCourses.size() > 0) {
            Course nextCourse = instantiatedCourses.get(0);
            ExecutionState.setCurrentCourse(nextCourse);
            List<Schedule> nextCourseSchedules = scheduler.permutationScheduler(nextCourse);
            Schedule nextBase = CLI.promptUserBaseSchedule(nextCourseSchedules);
            if (nextBase == null) {
                ExecutionState.setGenerationMode(allPermutations);
            } else {
                instantiatedCourses.remove(0);
                scheduler.setBaseSchedule(nextBase);
            }
        }

        ExecutionState.setGenerationMode(allPermutations);

        // call the scheduler to give us all schedules given these courses, filters, and
        // base
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

            } catch (IOException | IllegalStateException exception) {
                /**
                 * In case something goes wrong with the API for a specific course code, we print
                 * the code and the exception that is thrown.
                 */
                System.out.println(ConsoleColours.RED);
                System.out.println(
                        "Exception occurred for course "
                                + courseInput
                                + " with the following message: \n"
                                + exception);
                System.out.println(ConsoleColours.RESET);
                System.out.println("Please re-enter your courses.");
            }
        }
        Collections.sort(
                courses,
                new Comparator<Course>() {
                    public int compare(Course c1, Course c2) {
                        int c1_sections = c1.getLectures().size() + c1.getTutorials().size();
                        int c2_sections = c2.getLectures().size() + c2.getTutorials().size();
                        if (c1_sections > c2_sections) return 1;
                        if (c2_sections > c1_sections) return -1;
                        return 0;
                    }
                });
        return courses;
    }
}
