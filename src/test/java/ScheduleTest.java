import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ScheduleTest {
    Schedule schedule;
    ArrayList<Session> lecsToAdd;
    ArrayList<Session> tutsToAdd;
    Session lec1, lec2, lec3;
    Session tut1, tut2, tut3;

    @Before
    public void setUp() {
        schedule = new Schedule();
//        lec1 = new Session("LEC-0101",
//                LocalTime.of(9,00,00),
//                LocalTime.of(10,00,00));
//        lec2 = new Session("LEC-0201",
//                LocalTime.of(4,00,00),
//                LocalTime.of(5,00,00));
//        lec3 = new Session("LEC-0301",
//                LocalTime.of(1,00,00),
//                LocalTime.of(2,00,00));
        //tut1 = new Session();
        //tut2 = new Session();
        //tut3 = new Session();
        lecsToAdd = new ArrayList<>(List.of(lec1, lec2, lec3));
        tutsToAdd = new ArrayList<>(List.of(tut1, tut2, tut3));
    }

    @Test(timeout = 50)
    public void testAddLectures() {


    }

    @Test(timeout = 50)
    public void testAddTutorials() {


    }


}
