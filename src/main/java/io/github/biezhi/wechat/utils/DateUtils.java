package io.github.biezhi.wechat.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author biezhi
 * @date 2018/1/19
 */
public class DateUtils {

    public static void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static final DateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");

    public static String getDateString() {
        return format1.format(new Date());
    }

}
