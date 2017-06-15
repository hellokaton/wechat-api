package io.github.biezhi.wechat;

import io.github.biezhi.wechat.util.Environment;

public class Application {

    public static void main(String[] args) {
        try {

            Constant.environment = Environment.of("classpath:environment.properties");

            WechatRobot wechatRobot = new WechatRobot();
            wechatRobot.showQrCode();
            while (!Constant.HTTP_OK.equals(wechatRobot.waitForLogin())) {
                Thread.sleep(2000);
            }
            wechatRobot.closeQrWindow();
            wechatRobot.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}