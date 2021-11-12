import java.util.ArrayList;
import java.util.Scanner;

/**
 * The user interface of the program.
 */

public class CommandLineInterface {

    public CommandLineInterface() {}

    /**
     * Method that prompts user for input, eventually returning a List of course IDs that they will be taking. The
     * list should be sorted from greatest to least priority.
     *
     * @return a list of desired course codes from greatest to least priority.
     */
    public static ArrayList<String> promptUser() {
        Scanner scanner = new Scanner(System.in);
        Boolean input = false;
        int numCourses = 0;
        while (!input) {
            System.out.println("How many courses would you like information for?");
            try {
                numCourses = Integer.parseInt(scanner.nextLine());
                input = true;

            } catch (Exception NumberFormatException) {
                System.out.println("Please give an integer input");
            }
        }
        ArrayList<String> courses = new ArrayList<>();
        for (int a = 0; a < numCourses; a++) {
            System.out.println("Please give the course code of one of your courses");
            String course = scanner.nextLine();
            courses.add(course);
        }
        scanner.close();
        return courses;
    }

}
