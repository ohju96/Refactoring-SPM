package project.SPM.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class WeatherDateUtil {

    public static String getLongDateTime(Object time) {
        return getLongDateTime(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getLongDateTime(Integer time) {
        return getLongDateTime(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getLongDateTime(Object time, String fm) {
        return getLongDateTime((Integer) time, fm);
    }

    public static String getLongDateTime(Integer time, String fm) {
        Instant instant = Instant.ofEpochSecond(time); // Java 1.8부터 추가된 날짜 객체
        return DateTimeFormatter.ofPattern(fm)
                .withZone(ZoneId.systemDefault())
                .format(instant);
    }
}
