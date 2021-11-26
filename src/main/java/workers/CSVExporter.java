package workers;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import util.InvalidSessionException;

public class CSVExporter extends Exporter {

    private int num = 0;
    private final ZoneId zoneId = ZoneId.of("-4");
    private final LocalDate now = LocalDate.now(zoneId);
    private final int startYear = now.getMonthValue() < 9 ? now.getYear() - 1 : now.getYear();
    private final int endYear = startYear + 1;
    private final LocalDate FALL_SEMESTER_START_DATE = LocalDate.of(startYear, 9, 9);
    private final LocalDate FALL_SEMESTER_END_DATE = LocalDate.of(startYear, 12, 10);
    private final LocalDate WINTER_SEMESTER_START_DATE = LocalDate.of(endYear, 1, 10);
    private final LocalDate WINTER_SEMESTER_END_DATE = LocalDate.of(endYear, 4, 11);

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

    private void generateCSVSchedule(Schedule schedule, Writer writer) {
        StringBuilder rawSchedule = new StringBuilder();
        rawSchedule.append("Subject,Start Date,Start Time,End Date,End Time,Location\n");
        for (Section tutorial : schedule.getTutorials()) {
            for (Timeslot timeslot : tutorial.getTimes()) {
                try {
                    rawSchedule.append(
                            scheduleTimeslot(tutorial.getName(), tutorial.getSession(), timeslot));
                } catch (InvalidSessionException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Section lecture : schedule.getLectures()) {
            for (Timeslot timeslot : lecture.getTimes()) {
                try {
                    rawSchedule.append(
                            scheduleTimeslot(lecture.getName(), lecture.getSession(), timeslot));
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

    private String scheduleTimeslot(String name, String session, Timeslot timeslot)
            throws InvalidSessionException {
        LocalDate termStartDate, termEndDate;
        switch (session) {
            case "F":
                termStartDate = FALL_SEMESTER_START_DATE;
                termEndDate = FALL_SEMESTER_END_DATE;
                break;
            case "S":
                termStartDate = WINTER_SEMESTER_START_DATE;
                termEndDate = WINTER_SEMESTER_END_DATE;
                break;
            case "Y":
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

    public String toString(int id) throws IOException {
        File file =
                new File(
                        new File("")
                                .getAbsolutePath()
                                .concat("/output")
                                .concat(
                                        "/CSVSchedule_"
                                                + startYear
                                                + "-"
                                                + endYear
                                                + "_"
                                                + id
                                                + ".csv"));
        return Files.readString(Path.of(file.getPath()));
    }
}
