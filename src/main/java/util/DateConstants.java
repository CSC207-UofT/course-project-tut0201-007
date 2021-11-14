package util;

import java.time.LocalDate;
import java.time.ZoneId;

public class DateConstants {
    // idk this feels kinda gross

    public static final ZoneId zoneId = ZoneId.of("-4");
    private static LocalDate now = LocalDate.now(zoneId);
    private static int startYear = now.getMonthValue() < 9 ? now.getYear() - 1 : now.getYear();
    public static final LocalDate FALL_SEMESTER_START_DATE = LocalDate.of(startYear, 9, 9);
    public static final LocalDate FALL_SEMESTER_END_DATE = LocalDate.of(startYear, 12, 10);
    public static final LocalDate WINTER_SEMESTER_START_DATE = LocalDate.of(startYear + 1, 1, 10);
    public static final LocalDate WINTER_SEMESTER_END_DATE = LocalDate.of(startYear + 1, 4, 11);
}
