package mfdevelopement.appsolution.parser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeParser {

    private final String LOG_TAG = "DateTimeParser";

    private static String getDateCurrentTimeZone(long timestampUtc, String dateFormat) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestampUtc * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            return sdf.format(new Date(timestampUtc*1000));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getHoursMinutes(long timestamp) {
        String dateFormat = "HH:mm";
        return getDateCurrentTimeZone(timestamp, dateFormat);
    }

    public static String getDate(long timestamp) {
        String dateFormat = "dd.MM.yyyy";
        return getDateCurrentTimeZone(timestamp, dateFormat);
    }

    public static String getDateDayname(long timestamp) {
        String dateFormat = "EEE dd.MM.yyyy";
        return getDateCurrentTimeZone(timestamp, dateFormat);
    }

    public static String getDateTimeLong(long timestamp) {
        String dateFormat = "yyyy-MM-dd HH:mm";
        return getDateCurrentTimeZone(timestamp, dateFormat);
    }

    public static int getYear(long timestamp) {
        String dateFormat = "yyyy";
        return Integer.valueOf(getDateCurrentTimeZone(timestamp, dateFormat));
    }

    public static int getMoth(long timestamp) {
        String dateFormat = "MM";
        return Integer.valueOf(getDateCurrentTimeZone(timestamp, dateFormat));
    }

    public static int getDay(long timestamp) {
        String dateFormat = "dd";
        return Integer.valueOf(getDateCurrentTimeZone(timestamp, dateFormat));
    }
}
