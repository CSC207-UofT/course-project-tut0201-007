package workersTests;

import controllers.Controller;
import entities.Course;
import entities.Schedule;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import workers.*;

public class ICSExporterTest {
    Importer importer;
    ICSExporter exporter;

    @Before
    public void setUp() {
        importer = new ICSImporter();
        exporter = new ICSExporter();
    }

    @Test(timeout = 5000)
    public void testExportWithDefaultName() {
        Scheduler sr = new Scheduler();
        List<String> courseCodes = new ArrayList<>();
        courseCodes.add("TST102Y");
        courseCodes.add("TST103Y");
        List<Course> courses = Controller.courseInstantiator(courseCodes);
        Schedule s = sr.createBasicSchedule(courses);
        String fileName = "schedule0";
        exporter.outputSchedule(s);
        File file =
                new File(
                        new File("")
                                .getAbsolutePath()
                                .concat("/output")
                                .concat("/" + fileName + ".ics"));
        assert (file.exists());
    }

    @Test(timeout = 10000)
    public void testExportWithCustomName() {
        Scheduler sr = new Scheduler();
        List<String> courseCodes = new ArrayList<>();
        courseCodes.add("TST102Y");
        courseCodes.add("TST103Y");
        List<Course> courses = Controller.courseInstantiator(courseCodes);
        Schedule s = sr.createBasicSchedule(courses);
        String fileName = "custom_schedule_for_testing";
        exporter.outputSchedule(s, fileName);
        File file =
                new File(
                        new File("")
                                .getAbsolutePath()
                                .concat("/output")
                                .concat("/" + fileName + ".ics"));
        assert (file.exists());
    }
}
