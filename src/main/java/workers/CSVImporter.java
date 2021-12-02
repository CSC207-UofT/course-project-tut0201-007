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

public class CSVImporter implements Importer{

    @Override
    public Schedule importSchedule(Reader reader) {
        BufferedReader br = new BufferedReader(reader);
        Schedule schedule = new Schedule();
        Map<String, Section> sectionsByName = new HashMap<>();

        try {
            String line;
            while((line=br.readLine())!=null)
            {
                List<String> contents = Arrays.asList(line.split(","));
                if (!contents.get(0).equals("Subject") && !sectionsByName.containsKey(contents.get(0))) {
                    Timeslot timeslot =
                            new Timeslot(
                                    LocalTime.parse(contents.get(2)),
                                    LocalTime.parse(contents.get(4)),
                                    LocalDateTime.parse(contents.get(1)).getDayOfWeek(),
                                    contents.get(5),
                                    contents.get(5).charAt(-1));
                    sectionsByName.put(contents.get(0), new Section(contents.get(0)));
                    sectionsByName.get(contents.get(0)).addTime(timeslot);
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
