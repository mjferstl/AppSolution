package mfdevelopement.appsolution.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeParser {

    private static String getDateCurrentTimeZone(long timestamp, String dateFormat) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            Date currenTimeZone = calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
        String dateFormat = "EEE. dd.MM.yyyy";
        return getDateCurrentTimeZone(timestamp, dateFormat);
    }

    public static String getDateTimeLong(long timestamp) {
        String dateFormat = "yyyy-MM-dd HH:mm";
        return getDateCurrentTimeZone(timestamp, dateFormat);
    }
}