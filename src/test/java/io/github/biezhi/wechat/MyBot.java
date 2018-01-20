package io.github.biezhi.wechat;

import io.github.biezhi.wechat.constant.Config;
import lombok.extern.slf4j.Slf4j;

/**
 * @author biezhi
 * @date 2018/1/19
 */
@Slf4j
public class MyBot extends WeChatBot {

    public MyBot(Config config) {
        super(config);
    }


    public static void main(String[] args) {
        new MyBot(Config.me().showTerminal(true)).start();
    }

}
