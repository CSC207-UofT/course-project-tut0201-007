package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PromptHelpers {

    public static List<String> promptUserStrings(String question, int num) {
        List<String> ret = new ArrayList<>();
        for (int a = 0; a < num; a++) {
            ret.add(promptUserString(question));
        }
        return ret;
    }

    public static int promptUserInt(String question) {
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
        scanner.close();
        return numCourses;
    }

    public static List<Integer> promptUserInts(String question, int num) {
        List<Integer> ret = new ArrayList<>();
        for (int a = 0; a < num; a++) {
            ret.add(promptUserInt(question));
        }
        return ret;
    }

    public static String promptUserString(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(question);
        String input = scanner.nextLine();
        scanner.close();
        return input;
    }

    public static void promptYNSelection() {
        System.out.println(
                " • Press 1 for " + ConsoleColours.GREEN_BOLD + "YES \n" + ConsoleColours.RESET
                + " • Press 0 for " + ConsoleColours.RED_BOLD + "NO\n" + ConsoleColours.RESET
                + "Press 'Q' to quit selection.");
    }

    public static void promptGeneralSelection(String option1, String option2) {
        System.out.format(
                " • Press 1 for " + ConsoleColours.BLUE + "%s\n" + ConsoleColours.RESET
                        + " • Press 0 for " + ConsoleColours.BLUE + "%s\n" + ConsoleColours.RESET
                        + "Press 'Q' to quit selection.\n", option1, option2);
    }

}
