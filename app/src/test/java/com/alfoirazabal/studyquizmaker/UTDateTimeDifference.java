package com.alfoirazabal.studyquizmaker;

import com.alfoirazabal.studyquizmaker.helpers.dates.DateTimeDifference;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UTDateTimeDifference {

    @Test
    public void testTimeDifferencePrintSeconds() {
        Date oldDate = new Date("2020/01/01 23:59:50");
        Date newDate = new Date("2020/01/02 00:00:12");
        DateTimeDifference dateTimeParser = new DateTimeDifference(newDate, oldDate);
        String stringedDifference = dateTimeParser.toString();
        assertEquals("22'.000", stringedDifference);
    }

    @Test
    public void testTimeDifferencePrintMinutes() {
        Date oldDate = new Date("2020/01/01 08:00:00");
        Date newDate = new Date("2020/01/01 08:12:56");
        DateTimeDifference dateTimeParser = new DateTimeDifference(oldDate, newDate);
        String stringedDifference = dateTimeParser.toString();
        assertEquals("12:56", stringedDifference);
    }

    @Test
    public void testTimeDifferencePrintHours() {
        Date oldDate = new Date("2020/01/31 23:55:12");
        Date newDate = new Date("2020/02/01 02:57:20");
        DateTimeDifference dateTimeParser = new DateTimeDifference(newDate, oldDate);
        String stringedDifference = dateTimeParser.toString();
        assertEquals("3:02:08", stringedDifference);
    }

    @Test
    public void testTimeDifferencePrintDays() {
        Date oldDate = new Date("2020/01/31 23:55:12");
        Date newDate = new Date("2020/02/02 02:57:20");
        DateTimeDifference dateTimeParser = new DateTimeDifference(newDate, oldDate);
        String stringedDifference = dateTimeParser.toString();
        assertEquals("1:03:02:08", stringedDifference);
    }
}