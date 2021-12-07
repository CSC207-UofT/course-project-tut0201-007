package workersTests;


import controllers.Controller;
import entities.Course;
import entities.Schedule;
import org.junit.Before;
import org.junit.Test;
import workers.*;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ICSExporterTest {
    Importer importer;
    ICSExporter exporter;
    @Before
    public void setUp() {
        importer = new ICSImporter();
        exporter = new ICSExporter();
    }

    @Test(timeout = 5000)
    public void testInvertability(){
        Scheduler sr = new Scheduler();
        List<String> courseCodes = new ArrayList<>();
        courseCodes.add("TST102Y");
        courseCodes.add("TST103Y");
        List<Course> courses = Controller.courseInstantiator(courseCodes);
        Schedule s = sr.createBasicSchedule(courses);
        StringWriter writer = new StringWriter();
        exporter.outputSchedule(s, writer);
        StringReader reader = new StringReader(writer.toString());
        Schedule imported = importer.importSchedule(reader);
        assertEquals(s, imported);
    }

    @Test(timeout = 5000)
    public void testExportWithCustomName(){
        Scheduler sr = new Scheduler();
        List<String> courseCodes = new ArrayList<>();
        courseCodes.add("TST102Y");
        courseCodes.add("TST103Y");
        List<Course> courses = Controller.courseInstantiator(courseCodes);
        Schedule s = sr.createBasicSchedule(courses);
        String fileName = "custom_schedule_for_testing";
        exporter.outputSchedule(s,fileName);
        File file =  new File(new File("").getAbsolutePath().concat("/output").concat("/" + fileName + ".ics"));
        assert(file.exists());

    }
}
