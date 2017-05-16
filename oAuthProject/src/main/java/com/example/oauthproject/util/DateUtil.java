package com.example.oauthproject.util;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
public class DateUtil {
	private static final String DEFAULUT_TIMEZONE = "UTC+00:00";
	public static final DateTimeFormatter CLIENT_DATE_TIME_ZONE_FORMATTER = DateTimeFormat.forPattern("MM-dd-yyyy HH:mm:ss");
	public static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
        @Override
        public DateFormat get() {
            return super.get();
        }
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        }
        @Override
        public void remove() {
            super.remove();
        }
        @Override
        public void set(DateFormat value) {
            super.set(value);
        }
    };
	public static String getCurrentTimeByTimeZone(String clientTimeZone) throws IOException {
        String reqTimeZone = clientTimeZone;
        if (StringUtils.isBlank(clientTimeZone)) {
            reqTimeZone = DEFAULUT_TIMEZONE;
        }
        reqTimeZone = reqTimeZone.replaceFirst("UTC", "");
        LoggerUtil.logInfo("reqTimeZone    "+reqTimeZone);
        DateTime original = new DateTime();
        // DateTime original = new DateTime(((StandardTimeService)
        // BaseService.getContext().getBean("StandardTimeService")).getNistTime());
        DateTimeZone zone = DateTimeZone.forID(reqTimeZone);
        DateTime zoned = original.withZone(zone);
        LoggerUtil.logInfo("Time Zoned Time:        "+zoned.toString(CLIENT_DATE_TIME_ZONE_FORMATTER));
        return (zoned.toString(CLIENT_DATE_TIME_ZONE_FORMATTER));
    }
	public static Date convertByTimeZone(Date forDateTime,String timezone) throws IOException {
		LoggerUtil.logInfo("@@@@@@@@@@@@@ Current:   "+forDateTime);
        DateTime zoned = getUserTime(forDateTime,timezone);
        try {
            return df.get().parse(zoned.toString("MM-dd-yyyy HH:mm:ss"));
        } catch (ParseException e) {
            e.printStackTrace();
            return zoned.toDate();
        }
    }
	 public static DateTime getUserTime(Date forDateTime,String reqTimeZone) {
        if (StringUtils.isBlank(reqTimeZone)) {
            reqTimeZone = DEFAULUT_TIMEZONE;
        }
        LoggerUtil.logInfo("reqTimeZone:   "+reqTimeZone);
        reqTimeZone = reqTimeZone.replaceFirst("UTC", "");
        DateTime original = new DateTime(forDateTime);
        LoggerUtil.logInfo("original:    "+original);
        DateTimeZone zone = DateTimeZone.forID(reqTimeZone);
        DateTime zoned = original.withZone(zone);
        LoggerUtil.logInfo("@@@@@@@@@@@@@ Change:   "+zoned);
        return zoned;
    }
}