package com.chris.thomson.midlandsriders.Helpers;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateHelper {

    public static long getDaysBetween(Date date) {

        long diff = date.getTime() - new Date().getTime();

        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

    }

}
