package util;

import entities.Course;
import java.util.List;
import java.util.Scanner;

/** A class that allows course information to be displayed to the user */
public class CourseInfoDisplay {
    List<Course> courses;

    public CourseInfoDisplay(List<Course> courses) {
        this.courses = courses;
    }

    public void displayInfo() {
        Scanner input = new Scanner(System.in);
        int numberOfCourses = courses.size() - 1;
        int courseNumber = 0;
        char userActivity = 'W';

        while (userActivity != 'X') {
            Course currentCourse = courses.get(courseNumber);
            System.out.println();
            System.out.println(
                    "Showing information for course "
                            + ConsoleColours.WHITE_BOLD_BRIGHT
                            + "("
                            + (courseNumber + 1)
                            + "/"
                            + (numberOfCourses + 1)
                            + ")"
                            + ConsoleColours.RESET
                            + ":");
            printDisplay(currentCourse);
            System.out.println(
                    " • Press 'Q' to" + ConsoleColours.RED + " quit." + ConsoleColours.RESET);
            System.out.println(
                    " • Press '>' to view the"
                            + ConsoleColours.BLUE
                            + " next course."
                            + ConsoleColours.RESET);
            System.out.println(
                    " • Press '<' to view the"
                            + ConsoleColours.BLUE
                            + " previous course."
                            + ConsoleColours.RESET);
            System.out.println(
                    " • Press 'X' to "
                            + ConsoleColours.BLUE
                            + "create a schedule with these courses."
                            + ConsoleColours.RESET);

            char userInput = input.next().charAt(0);
            switch (userInput) {
                case 'Q':
                    System.out.println("Quitting program...");
                    System.exit(0);
                case '<':
                    if (courseNumber > 0) {
                        courseNumber--;
                    } else {
                        System.out.print(ConsoleColours.RED);
                        System.out.println("No course before this one.");
                        System.out.print(ConsoleColours.RESET);
                    }
                    break;
                case '>':
                    if (courseNumber < numberOfCourses) {
                        courseNumber++;
                    } else {
                        System.out.print(ConsoleColours.RED);
                        System.out.println("No course after this one.");
                        System.out.print(ConsoleColours.RESET);
                    }
                    break;
                case 'X':
                    StringBuilder coursesString = new StringBuilder();
                    for (Course course : courses) {
                        coursesString.append(course.getCourseId()).append(" ");
                    }
                    System.out.println(
                            ConsoleColours.GREEN
                                    + "Creating a Schedule with the courses: "
                                    + ConsoleColours.RESET
                                    + coursesString);
                    userActivity = 'X';
                    break;
            }
        }
    }

    private void printDisplay(Course course) {
        String description = course.getCourseDesc();
        System.out.println(
                ConsoleColours.BLUE_BOLD_BRIGHT
                        + "-+- "
                        + course.getCourseId()
                        + " -+-"
                        + ConsoleColours.RESET);
        System.out.println(
                "---------------------------------------------------------------------------------------------");
        System.out.println(
                ConsoleColours.WHITE_BOLD_BRIGHT + "Course Description:" + ConsoleColours.RESET);
        for (String s : description.split("(?<=\\.) (?=[A-Z])")) {
            System.out.println(s);
        }
        System.out.println(
                "---------------------------------------------------------------------------------------------");
        System.out.println(
                "Exclusions: "
                        + ConsoleColours.BLUE
                        + course.getExclusions().toString()
                        + ConsoleColours.RESET);
        System.out.println(
                "Corequisites: "
                        + ConsoleColours.BLUE
                        + course.getCorequisites().toString()
                        + ConsoleColours.RESET);
    }
}
