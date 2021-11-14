package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/** The user interface of the program. */
public class CommandLineInterface {

    public CommandLineInterface() {}

    /**
     * Method that prompts user for input, eventually returning a List of course IDs that they will
     * be taking. The list should be sorted from greatest to least priority.
     *
     * @return a list of desired course codes from greatest to least priority.
     */
    public static ArrayList<ArrayList<String>> promptUser() {
        Scanner scanner = new Scanner(System.in);
        boolean input = false;
        int numCourses = 0;
        ArrayList<ArrayList<String>> output = new ArrayList<>();

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
        System.out.println("Here are potential filters to select: ");
        System.out.println("1. Interval (Enforce space between courses)");
        System.out.println("2. In person (Enforce in person vs online delivery methods)");
        String filters = scanner.nextLine();
        ArrayList<String> parsed = parseString(filters);
        if (parsed.contains("1")){
            System.out.println("How long of an interval would you like to enforce between courses?");
            String interval = scanner.nextLine();
            output.add(new ArrayList<>(Collections.singleton(interval)));
        }
        scanner.close();

        output.add(courses);
        output.add(parsed);
        return output;
    }

    public static ArrayList<String> parseString (String str) {

        String[] string = str.replaceAll("\\[", "").replaceAll("]", "")
                .split(",");

        ArrayList<String> arr = new ArrayList<>();
        Collections.addAll(arr, string);

        return arr;
    }
}
