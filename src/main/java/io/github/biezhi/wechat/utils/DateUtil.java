package io.github.biezhi.wechat.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author biezhi
 * @date 2018/1/19
 */
public class DateUtil {

    public static void sleep(long ms){
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
