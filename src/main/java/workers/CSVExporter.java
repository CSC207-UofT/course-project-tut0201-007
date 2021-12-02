package workers;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import util.InvalidSessionException;

public class CSVExporter extends Exporter {

    @Override
    public void outputSchedule(Schedule schedule) {
        File outputDir = new File(new File("").getAbsolutePath().concat("/output"));
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
        try {
            Writer writer =
                    new FileWriter(
                            outputDir
                                    .getAbsolutePath()
                                    .concat(
                                            "/CSVSchedule_"
                                                    + startYear
                                                    + "-"
                                                    + endYear
                                                    + "_"
                                                    + num
                                                    + ".csv"));
            generateCSVSchedule(schedule, writer);
            num += 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void outputSchedule(Schedule schedule, String name) {
        File outputDir = new File(new File("").getAbsolutePath().concat("/output"));
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
        try {
            Writer writer = new FileWriter(outputDir.getAbsolutePath().concat("/" + name + ".csv"));
            generateCSVSchedule(schedule, writer);
            num += 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts all Timeslots within a Schedule into .csv format.
     *
     * @param schedule A Schedule object to output as .csv
     * @param writer A Writer object used to output the .csv
     */
    private void generateCSVSchedule(Schedule schedule, Writer writer) {
        StringBuilder rawSchedule = new StringBuilder();
        rawSchedule.append("Subject,Start Date,Start Time,End Date,End Time,Location\n");
        for (Section tutorial : schedule.getTutorials()) {
            for (Timeslot timeslot : tutorial.getTimes()) {
                try {
                    rawSchedule.append(
                            scheduleTimeslot(tutorial.getName(), timeslot.getSession(), timeslot));
                } catch (InvalidSessionException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Section lecture : schedule.getLectures()) {
            for (Timeslot timeslot : lecture.getTimes()) {
                try {
                    rawSchedule.append(
                            scheduleTimeslot(lecture.getName(), timeslot.getSession(), timeslot));
                } catch (InvalidSessionException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            writer.write(rawSchedule.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Since .csv does not support recurring events, this method schedules a timeslot every week
     * from the start date to the end date.
     *
     * @param name The name of the Timeslot object i.e. CSC207 LEC0101 Y
     * @param session The session the Timeslot object (F/S/Y)
     * @param timeslot The Timeslot to schedule
     */
    private String scheduleTimeslot(String name, char session, Timeslot timeslot)
            throws InvalidSessionException {
        LocalDate termStartDate, termEndDate;
        switch (session) {
            case 'F':
                termStartDate = FALL_SEMESTER_START_DATE;
                termEndDate = FALL_SEMESTER_END_DATE;
                break;
            case 'S':
                termStartDate = WINTER_SEMESTER_START_DATE;
                termEndDate = WINTER_SEMESTER_END_DATE;
                break;
            case 'Y':
                termStartDate = FALL_SEMESTER_START_DATE;
                termEndDate = WINTER_SEMESTER_END_DATE;
                break;
            default:
                throw new InvalidSessionException(
                        String.format(
                                "%s has invalid session %s. It should be either F, Y, or S",
                                name, session));
        }

        StringBuilder rawTimeslot = new StringBuilder();
        LocalDate currDay = getStartingWeekDate(termStartDate, timeslot.getDay());
        while (currDay.isBefore(termEndDate)) {
            rawTimeslot
                    .append(name)
                    .append(",")
                    .append(currDay)
                    .append(",")
                    .append(timeslot.getStart())
                    .append(",")
                    .append(currDay)
                    .append(",")
                    .append(timeslot.getEnd())
                    .append(",")
                    .append(timeslot.getRoom())
                    .append("\n");
            currDay = currDay.plusDays(7);
        }
        return rawTimeslot.toString();
    }

    /**
     * A method to return a raw String representation of the .csv file, for testing.
     *
     * @param fileName The name of the file (CSVSchedule_2021-2022_0.csv has name CSVSchedule_2021-2022_0)
     */
    public String toString(String fileName) throws IOException {
        File file =
                new File(
                        new File("")
                                .getAbsolutePath()
                                .concat("/output")
                                .concat(
                                        "/" + fileName + ".csv"));
        return Files.readString(Path.of(file.getPath()));
    }
}
