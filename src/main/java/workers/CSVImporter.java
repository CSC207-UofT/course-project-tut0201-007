package workers;

import entities.Schedule;
import entities.Section;
import entities.Timeslot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class CSVImporter implements Importer {

    @Override
    public Schedule importSchedule(Reader reader) {
        BufferedReader br = new BufferedReader(reader);
        Schedule schedule = new Schedule();
        List<Timeslot> timeslots = new ArrayList<>();

        try {
            String line = br.readLine();
            int count = 0;
            while ((line = br.readLine()) != null) {
                count++;
                if (count == 13) {
                    if (line.contains("LEC")) {
                        List<Timeslot> timeslot = new ArrayList<>();
                        timeslot.add(lineToTimeslot(line));
                        schedule.addLecture(new Section(line.split(",")[0], timeslot));
                    }
                    timeslots.add(lineToTimeslot(line));
                    count = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schedule;
    }

    private Timeslot lineToTimeslot(String line) {
        List<String> contents = Arrays.asList(line.split(","));
        return new Timeslot(
                        LocalTime.parse(contents.get(2)),
                        LocalTime.parse(contents.get(4)),
                        LocalDateTime.parse(contents.get(1)).getDayOfWeek(),
                        contents.get(5),
                        contents.get(0).charAt(contents.get(0).length() - 1));
    }

}
