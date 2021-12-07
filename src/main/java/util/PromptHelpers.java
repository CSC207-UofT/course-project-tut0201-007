package util;

public class PromptHelpers {

    public static void promptYNSelection() {
        System.out.println(
                " • Press 1 for "
                        + ConsoleColours.GREEN_BOLD
                        + "YES \n"
                        + ConsoleColours.RESET
                        + " • Press 0 for "
                        + ConsoleColours.RED_BOLD
                        + "NO\n"
                        + ConsoleColours.RESET
                        + "Press 'Q' to quit selection.");
    }

    public static void promptGeneralSelection(String option1, String option2) {
        System.out.format(
                " • Press 1 for "
                        + ConsoleColours.BLUE
                        + "%s\n"
                        + ConsoleColours.RESET
                        + " • Press 0 for "
                        + ConsoleColours.BLUE
                        + "%s\n"
                        + ConsoleColours.RESET
                        + "Press 'Q' to quit selection.\n",
                option1,
                option2);
    }
}
