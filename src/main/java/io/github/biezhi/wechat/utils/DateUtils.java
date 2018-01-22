package io.github.biezhi.wechat.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 日期工具类
 *
 * @author biezhi
 * @date 2018/1/19
 */
@Slf4j
public class DateUtils {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    public static void sendSleep(){
        int rand = 2 + WeChatUtils.random(100, 3000);
        DateUtils.sleep(rand);
    }

    /**
     * 休眠，单位: 毫秒
     *
     * @param ms
     */
    public static void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            log.error("", e);
        }
    }

    /**
     * 获取字符串日期
     *
     * @return
     */
    public static String getDateString() {
        return DATE_FORMAT.format(new Date());
    }

}
