package util;

import java.util.ArrayList;
import java.util.Scanner;

class PromptHelpers {
    public static ArrayList<String> promptUserStrings(String question, int num) {
        ArrayList<String> ret = new ArrayList<>();
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

    public static String promptUserString(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(question);
        String input = scanner.nextLine();
        scanner.close();
        return input;
    }
}
