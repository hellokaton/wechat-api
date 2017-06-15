package io.github.biezhi.wechat;

import io.github.biezhi.wechat.util.Environment;

import java.util.concurrent.TimeUnit;

/**
 * wechat启动程序
 */
public class Application {

    public static void main(String[] args) throws Exception {
        Constant.environment = Environment.of("classpath:config.properties");

        WechatRobot wechatRobot = new WechatRobot();
        wechatRobot.showQrCode();
        while (!Constant.HTTP_OK.equals(wechatRobot.waitForLogin())) {
            TimeUnit.SECONDS.sleep(2);
        }
        wechatRobot.closeQrWindow();
        wechatRobot.start();
    }

}