package com.alfoirazabal.studyquizmaker.helpers.dates;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTimeDifference {

    private long dateMilisDiff;
    private int milliseconds;
    private int seconds;
    private int minutes;
    private int hours;
    private int days;

    public DateTimeDifference(
            Date date1,
            Date date2
    ) {
        this.dateMilisDiff = Math.abs(date1.getTime() - date2.getTime());
        this.milliseconds = (int)(dateMilisDiff % 1000);
        this.seconds = (int)((dateMilisDiff / 1000) % 60);
        this.minutes = (int)((dateMilisDiff / 60000) % 60);
        this.hours = (int)((dateMilisDiff / 3600000) % 24);
        this.days = (int)((dateMilisDiff / 84000000));
    }

    @Override
    public String toString() {
        if (this.dateMilisDiff < 60000) {
            return zeroForwardTil100(this.seconds) + "'." + zeroForwardTil1000(this.milliseconds);
        }
        else if (this.dateMilisDiff < 3600000) {
            return zeroForwardTil100(this.minutes) + ":" + zeroForwardTil100(this.seconds);
        }
        else if (this.dateMilisDiff < 84000000) {
            return this.hours + ":" + zeroForwardTil100(this.minutes) + ":" + zeroForwardTil100(this.seconds);
        }
        else {
            return this.days + ":" + zeroForwardTil100(this.hours) + ":" + zeroForwardTil100(this.minutes) +
                    ":" + zeroForwardTil100(this.seconds);
        }
    }

    private String zeroForwardTil100(int numericValue) {
        if (numericValue < 10) {
            return "0" + numericValue;
        }
        else {
            return String.valueOf(numericValue);
        }
    }

    private String zeroForwardTil1000(int numericValue) {
        if (numericValue < 10) {
            return "00" + numericValue;
        }
        else if (numericValue < 100) {
            return "0" + numericValue;
        }
        else {
            return String.valueOf(numericValue);
        }
    }
}
