package filtersTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import controllers.Controller;
import entities.Course;
import entities.Schedule;
import filters.InPersonFilter;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import workers.Scheduler;

public class InPersonFilterTest {
    InPersonFilter filterInPerson;
    InPersonFilter filterOnline;
    Scheduler scheduleCreator;

    @Before
    public void setUp() {
        filterInPerson = new InPersonFilter(true);
        filterOnline = new InPersonFilter(false);
        scheduleCreator = new Scheduler();
    }

    @Test(timeout = 5000)
    public void filterInPersonSucceed() {
        ArrayList<String> tst102 = new ArrayList<>();
        tst102.add("TST102Y");
        ArrayList<Course> courses = (ArrayList<Course>) Controller.courseInstantiator(tst102);
        Schedule schedule = scheduleCreator.createBasicSchedule(courses);
        assertTrue(filterInPerson.checkSchedule(schedule));
    }

    @Test(timeout = 2000)
    public void filterInPersonFail() {
        ArrayList<String> tst103 = new ArrayList<>();
        tst103.add("TST103Y");
        ArrayList<Course> courses = (ArrayList<Course>) Controller.courseInstantiator(tst103);
        Schedule schedule = scheduleCreator.createBasicSchedule(courses);
        System.out.println(schedule);
        assertFalse(filterInPerson.checkSchedule(schedule));
    }

    @Test(timeout = 6000)
    public void filterOnlineSucceed() {
        ArrayList<String> tst103 = new ArrayList<>();
        tst103.add("TST107F");
        ArrayList<Course> courses = (ArrayList<Course>) Controller.courseInstantiator(tst103);
        Schedule schedule = scheduleCreator.createBasicSchedule(courses);
        System.out.println(schedule);
        assertTrue(filterOnline.checkSchedule(schedule));
    }

    @Test(timeout = 2000)
    public void filterOnlineFail() {
        ArrayList<String> tst101 = new ArrayList<>();
        tst101.add("TST101Y");
        ArrayList<Course> courses = (ArrayList<Course>) Controller.courseInstantiator(tst101);
        Schedule schedule = scheduleCreator.createBasicSchedule(courses);
        assertFalse(filterOnline.checkSchedule(schedule));
    }
}
