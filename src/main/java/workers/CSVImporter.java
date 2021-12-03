package workers;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class CSVImporter implements Importer {

    @Override
    public Schedule importSchedule(Reader reader) {
        int semesterWeeks = 13;
        int currWeek = 1;
        BufferedReader br = new BufferedReader(reader);
        Schedule schedule = new Schedule();
        Map<String, Section> sectionsByName = new HashMap<>();

        try {
            String line;
            while ((line = br.readLine()) != null) {
                String name = line.split(",")[0];
                currWeek++;
                if ((currWeek - 1) % semesterWeeks == 0) {
                    if (sectionsByName.containsKey(name)) {
                        sectionsByName.get(name).addTime(lineToTimeslot(line));
                    } else {
                        List<Timeslot> timeslot = new ArrayList<>();
                        timeslot.add(lineToTimeslot(line));
                        sectionsByName.put(name, new Section(name, timeslot));
                    }
                    currWeek = 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Section section : sectionsByName.values()) {
            if (section.getName().contains("LEC")) {
                schedule.addLecture(section);
            } else {
                schedule.addTutorial(section);
            }
        }
        return schedule;
    }

    /**
     * Converts a line in the .csv file to a Timeslot object
     *
     * @param line The line to be converted
     * @return a Timeslot object corresponding to the line
     */
    private Timeslot lineToTimeslot(String line) {
        List<String> contents = Arrays.asList(line.split(","));
        return new Timeslot(
                LocalTime.parse(contents.get(2)),
                LocalTime.parse(contents.get(4)),
                LocalDate.parse(contents.get(1)).getDayOfWeek(),
                contents.get(5),
                LocalDate.parse(contents.get(1)).getYear()
                                == LocalDate.now(ZoneId.of("-4")).getYear()
                        ? 'F'
                        : 'S');
    }
}
