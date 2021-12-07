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

public class ImageExporterTest {
    Exporter exporter;

    @Before
    public void setUp() {
        exporter = new ImageExporter();
    }

    @Test(timeout = 5000)
    public void testExportWithDefaultName() {
        Scheduler sr = new Scheduler();
        List<String> courseCodes = new ArrayList<>();
        courseCodes.add("TST102Y");
        courseCodes.add("TST103Y");
        List<Course> courses = Controller.courseInstantiator(courseCodes);
        Schedule s = sr.createBasicSchedule(courses);
        String fileName = "schedule_image0";
        exporter.outputSchedule(s);
        File winterSchedule =
                new File(
                        new File("")
                                .getAbsolutePath()
                                .concat("/output")
                                .concat("/" + fileName + "_winter.jpg"));
        File fallSchedule =
                new File(
                        new File("")
                                .getAbsolutePath()
                                .concat("/output")
                                .concat("/" + fileName + "_fall.jpg"));
        assert (fallSchedule.exists() && winterSchedule.exists());
    }

    @Test(timeout = 5000)
    public void testExportWithCustomName() {
        Scheduler sr = new Scheduler();
        List<String> courseCodes = new ArrayList<>();
        courseCodes.add("TST102Y");
        courseCodes.add("TST103Y");
        List<Course> courses = Controller.courseInstantiator(courseCodes);
        Schedule s = sr.createBasicSchedule(courses);
        String fileName = "custom_schedule_for_testing";
        exporter.outputSchedule(s, fileName);
        File winterSchedule =
                new File(
                        new File("")
                                .getAbsolutePath()
                                .concat("/output")
                                .concat("/" + fileName + "_winter.jpg"));
        File fallSchedule =
                new File(
                        new File("")
                                .getAbsolutePath()
                                .concat("/output")
                                .concat("/" + fileName + "_fall.jpg"));
        assert (fallSchedule.exists() && winterSchedule.exists());
    }
}
