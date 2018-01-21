package io.github.biezhi.wechat;

import io.github.biezhi.wechat.api.annotation.Bind;
import io.github.biezhi.wechat.api.constant.Config;
import io.github.biezhi.wechat.api.enums.AccountType;
import io.github.biezhi.wechat.api.enums.MsgType;
import io.github.biezhi.wechat.api.model.WeChatMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 我的小机器人
 *
 * @author biezhi
 * @date 2018/1/19
 */
@Slf4j
public class MyBot extends WeChatBot {

    public MyBot(Config config) {
        super(config);
    }

    @Bind(msgType = MsgType.ALL, accountType = {AccountType.TYPE_FRIEND, AccountType.TYPE_GROUP})
    public void handleText(WeChatMessage message) {
        log.info("接收到 [{}] 的消息: {}", message.getName(), message.getText());
        this.api().sendText(message.getFromUserName(), message.getText() + " : 嘻嘻嘻 [坏笑]");
//        this.sendImage(message.getFromUserName(), "/Users/biezhi/Desktop/3849072.jpeg");
    }

    public static void main(String[] args) {
        new MyBot(Config.me().autoLogin(true).showTerminal(true)).start();
    }

}
