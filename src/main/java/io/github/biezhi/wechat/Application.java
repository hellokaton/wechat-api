package io.github.biezhi.wechat;

import io.github.biezhi.wechat.handle.SampleMessageHandler;
import io.github.biezhi.wechat.ui.StartUI;
import io.github.biezhi.wechat.model.Environment;

/**
 * wechat启动程序
 */
public class Application {

    public static void main(String[] args) throws Exception {
        System.setProperty("https.protocols", "TLSv1");
        System.setProperty("jsse.enableSNIExtension", "false");

        Environment environment = Environment.of("classpath:config.properties");

        StartUI startUI = new StartUI(environment);
        startUI.setMsgHandle(new SampleMessageHandler());
        startUI.start();
    }

}